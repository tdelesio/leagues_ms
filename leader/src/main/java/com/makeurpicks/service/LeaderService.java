package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.DoublePickView;
import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.PickView;
import com.makeurpicks.domain.PickView.PickStatus;
import com.makeurpicks.domain.PlayerView;
import com.makeurpicks.domain.SeasonStats;
import com.makeurpicks.domain.WeekView;
import com.makeurpicks.domain.WeekWinner;
import com.makeurpicks.domain.WinSummary;
import com.makeurpicks.exception.LeaderClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class LeaderService {

	@Autowired
	private WeekClient weekClient;
	
	@Autowired
	private LeagueClient leagueClient;
	
	@Autowired
	private PlayerClient playerClient;
	
	@Autowired
	private PickClient pickClient;
	
	@HystrixCommand(fallbackMethod="defaultGetWeeksBySeasonId")
	protected Iterable<WeekView> getWeeksBySeasonId(String id)
	{
		return weekClient.getWeeksBySeason(id);
	}
	
	protected Iterable<WeekView> defaultGetWeeksBySeasonId(String id)
	{
		throw new LeaderClientException();
	}
	
	@HystrixCommand(fallbackMethod="defaultGetLeague")
	protected LeagueView getLeague(String id)
	{
		return leagueClient.getLeagueById(id);
	}
	
	protected LeagueView defaultGetLeague(String id)
	{
		throw new LeaderClientException();
	}
	
	public Map<Integer, String> getWeekWinners(String leagueId)
	{
		//get all the weeks
//		league = dao.loadByPrimaryKey(League.class, league.getId());
//		List<Week> weeks = gameManager.getWeeksBySeason(league.getSeason());
		LeagueView leagueView = getLeague(leagueId);
		
		Iterable<WeekView> weeks = getWeeksBySeasonId(leagueView.getSeasonId());
		
		
		Map<Integer, String> weekWinners = new HashMap<Integer,String>();
		for (WeekView week : weeks)
		{
			StringBuffer stringBuffer = new StringBuffer();
			List<PlayerView> players = getWinningPlayersForWeek(leagueId, week.getId());
			for (PlayerView player:players)
			{
				stringBuffer.append(player.getUsername());
				stringBuffer.append(",");
			}
			stringBuffer.deleteCharAt(stringBuffer.length()-1);
			weekWinners.put(week.getWeekNumber(), stringBuffer.toString());
		}
		
		return weekWinners;
	}
	
	private List<PlayerView> getWinningPlayersForWeek(String leagueId, String weekId)
	{
		int winningWeek = getWinningScoreForLeagueAndWeek(leagueId, weekId);
		List<PlayerView> winners = new ArrayList<PlayerView>();
		Set<PlayerView> players = getPlayersForLeagueSortedByWeekWins(leagueId, weekId);
		for (PlayerView player:players)
		{
			if (player.getWins() == winningWeek)
				winners.add(player);
			else
				break;
		}
		
		return winners;
	}
	
	private Set<PlayerView> defaultGetPlayersInLeague(String leagueId)
	{
		throw new LeaderClientException();
	}
	
	@HystrixCommand(fallbackMethod="defaultGetPlayersInLeague")
	private Set<PlayerView> getPlayersInLeague(String leagueId)
	{
		return leagueClient.getPlayersInLeague(leagueId);
	}
	
	private int getWinningScoreForLeagueAndWeek(String leagueId, String weekId)
	{
		Set<PlayerView> players = getPlayersInLeague(leagueId);
		int highestNumber=0;
		for (PlayerView player: players)
		{
			SeasonStats seasonStats = getPlayersStatsForWeek(player, leagueId, weekId);
			if (seasonStats.getWins()>0&&seasonStats.getWins()>highestNumber)
			{
				highestNumber = seasonStats.getWins();
			}
			
		}
		return highestNumber;
	}
	
	private Iterable<PickView> defaultGetPicksByPlayerLeagueAndWeek(String playerId, String leagueId, String weekId)
	{
		throw new LeaderClientException();
	}
	
	@HystrixCommand(fallbackMethod="defaultGetPicksByPlayerLeagueAndWeek")
	private Iterable<PickView> getPicksByPlayerLeagueAndWeek(String playerId, String leagueId, String weekId)
	{
		return pickClient.getPicksByLeaguenWeekAndPlayer(leagueId, weekId, playerId);
	}
	
	@HystrixCommand(fallbackMethod="defaultGetDoublePick")
	private DoublePickView getDoublePick(String playerid, String leagueid, String weekid)
	{
		return pickClient.getDoublePick(leagueid, weekid, playerid);
	}
	
	private DoublePickView defaultGetDoublePick(String playerid, String leagueid, String weekid)
	{
		throw new LeaderClientException();
	}
	
	private SeasonStats getPlayersStatsForWeek(PlayerView player, String leagueId, String weekId)
	{
		SeasonStats seasonStats = new SeasonStats();
		seasonStats.setUsername(player.getUsername());
		seasonStats.setId(player.getId());
		Iterable<PickView> picks = getPicksByPlayerLeagueAndWeek(player.getId(), leagueId, weekId);
		DoublePickView doublePickView = getDoublePick(player.getId(), leagueId, weekId);
		for (PickView pick:picks)
		{
			if (pick.getPickOutcome().equals(PickStatus.WINNER))
			{
				seasonStats.addWin(1);
				if (doublePickView!=null && pick.getId().equals(doublePickView.getId()))
					seasonStats.addWin(1);
			} 
			else if (pick.getPickOutcome().equals(PickStatus.LOSER))
			{
				seasonStats.addLoses(1);
			}
		}
		
		return seasonStats;
	}
	
	private Set<PlayerView> getPlayersForLeagueSortedByWeekWins(String leagueId, String weekId)
	{
		Set<PlayerView> players = getPlayersInLeague(leagueId);
		for (PlayerView player:players)
		{
			SeasonStats seasonStats = getPlayersStatsForWeek(player, leagueId, weekId);
			player.setWins(seasonStats.getWins());
		}
		
		Collections.sort(new ArrayList(players), new Comparator<PlayerView>() {


			public int compare(PlayerView arg0, PlayerView arg1) {
				if (arg0.getWins()>arg1.getWins())
					return -1;
				else if (arg0.getWins()==arg1.getWins())
					return 0;
				else
					return 1;
			}
			
		
		});
		
		return players;
	}
	
	private Iterable<WeekView> defaultGetWeeksBySeason(String seasonId)
	{
		throw new LeaderClientException();
	}
	
	@HystrixCommand(fallbackMethod="defaultGetWeeksBySeason")
	private Iterable<WeekView> getWeeksBySeason(String seasonId)
	{
		return weekClient.getWeeksBySeason(seasonId);
	}
	
	public List<WinSummary> getWinSummary(String leagueId)
	{
		
		LeagueView league = getLeague(leagueId);
		
		Map<Integer, Integer> weekSplits = new HashMap<Integer, Integer>(17);
		Map<Integer, WeekWinner> weekTotal=null;
		WinSummary winSummary;
		int totalWins;
		
		//get all the weeks
//		List<Week> weeks = gameManager.getWeeksBySeason(league.getSeasonId());
		Iterable<WeekView> weeks = getWeeksBySeason(league.getSeasonId());
		
		//get all the players in the league
//		List<PlayerView> playersInLeague = playerManager.getPlayersInLeague(league);
		Set<PlayerView> playersInLeague = getPlayersInLeague(leagueId);
		
		//create a map that 
		Map<Integer, Integer> weekWinners = new HashMap<Integer, Integer>();
		//a winsummary for every player
		List<WinSummary> winSummaries = new ArrayList<WinSummary>(playersInLeague.size());
		
		//loop through all the players in the leauge
		for (PlayerView player: playersInLeague)
		{
			totalWins=0;
			
			//create a win summary for the player
			winSummary = new WinSummary(player.getId());
			weekTotal = new HashMap<Integer, WeekWinner>();
			for (WeekView week:weeks)
			{
				//get the number of wins the player had for the week
				SeasonStats seasonStats = getPlayersStatsForWeek(player, leagueId, week.getId());
				int winsForWeek = seasonStats.getWins();
				
				//put the numbers of wins the player had in a map with the key of week
				weekTotal.put(week.getWeekNumber(), new WeekWinner(winsForWeek));
				
				//increment the total wins
				totalWins+=winsForWeek;
				
				Integer winningWeekTotal = weekWinners.get(week);
				//trying to build the map of the winning weeks
				if (winningWeekTotal == null || winningWeekTotal<winsForWeek)
				{
					weekWinners.put(week.getWeekNumber(), winsForWeek);
				}
			}
			
			winSummary.setWeekTotal(weekTotal);
			winSummary.setNumberOfWins(totalWins);
			winSummaries.add(winSummary);
			
		}
		
		Collections.sort(winSummaries);
		
		int place = 1;
		int wins = -1;
		int tieCounter=1;
		int[] tieHolder = new int[] {0, 0, 0, 0, 0};
		for (WinSummary placeWin : winSummaries)
		{
			if (wins==-1)
			{
				wins = placeWin.getNumberOfWins();
			}
			else if (wins == placeWin.getNumberOfWins())
			{
				tieCounter++;
			}
			else
			{
				place += tieCounter;
				tieCounter=1;
				wins = placeWin.getNumberOfWins();
			}
			placeWin.setPlace(place);
			if (place == 1)
				tieHolder[0]++;
			else if (place == 2)
				tieHolder[1]++;
			else if (place == 3)
				tieHolder[2]++;
			else if (place == 4)
				tieHolder[3]++;
			else if (place == 5)
				tieHolder[4]++;

			Map<Integer, WeekWinner> weeks2 = placeWin.getWeekTotal();
			for (Integer key :weeks2.keySet())
			{
				int winningNumber = weekWinners.get(key);
				WeekWinner weekWinner = weeks2.get(key);
				if (winningNumber == weekWinner.getWins())
				{
					weekWinner.setWinner(true);
					int weekTiesCounter=0;
					if (weekSplits.get(key) != null)
						weekTiesCounter = weekSplits.get(key).intValue();
				
					weekTiesCounter++;
					weekSplits.put(key, weekTiesCounter);
					
				}
			}
		}
		
		for (int i=0;i<5;i++)
		{
			if (tieHolder[i]>1)
			{
				for (int j=0;j<tieHolder[i];j++)
				{
					int index = (i+j);
					winSummaries.get(index).setNumberOfPeopleSplitWith(tieHolder[i]);
				}
				
			}
			
		}
		
//		if (league.isBanker())
//			calculateWinnings(league, winSummaries, playersInLeague.size(), weekSplits);
		return winSummaries;
	}
	
	public List<SeasonStats> getPlayersPlusWinsInLeagueTX(String leagueId, String weekid)
	{
//		List<Player> players = getPlayersInLeague(leagueId);
		Set<PlayerView> players = getPlayersInLeague(leagueId);
		List<SeasonStats> seasonStats = new ArrayList<SeasonStats>(players.size());
		for (PlayerView player : players)
		{
//			seasonStats.add(picksManager.getPlayersStatsForWeek(player, new League(leagueId), new Week(weekid)));
			seasonStats.add(getPlayersStatsForWeek(player, leagueId, weekid));
		}
		Collections.sort(seasonStats, new Comparator<SeasonStats>() {
			
			public int compare(SeasonStats o1, SeasonStats o2) {
				if (o1.getWins() > o2.getWins())
					return -1;
				else if (o1.getWins() < o2.getWins())
					return 1;
				else return 0;
			}
			
		
		} );
		return seasonStats;
	}
	
//	protected void calculateWinnings(League league, List<WinSummary> winSummaries, int numberOfPlayersInLeague, Map<Integer, Integer> weekSplits)
//	{
//		double entryFeeTotalWin = numberOfPlayersInLeague*league.getEntryFee();
//		double weeklyWin = numberOfPlayersInLeague*league.getWeeklyFee();
//		double placementPoolWin=0;
//		double firstPlaceWin = entryFeeTotalWin*league.getFirstPlacePercent()*.01;
//		double secondPlaceWin = entryFeeTotalWin*league.getSecondPlacePercent()*.01;
//		double thirdPlaceWin = entryFeeTotalWin*league.getThirdPlacePercent()*.01;
//		double fourthPlaceWin = entryFeeTotalWin*league.getFourthPlacePercent()*.01;
//		double fifthPlaceWin = entryFeeTotalWin*league.getFifthPlacePercent()*.01;
//		
//		//calculate season totals
//		for (WinSummary winSummary:winSummaries)
//		{			
//			if (winSummary.getPlace()==1&&league.getFirstPlacePercent()!=0)
//			{
//				if (winSummary.getNumberOfPeopleSplitWith()==2)
//				{
//					placementPoolWin = (firstPlaceWin+secondPlaceWin)/2;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()==3)
//				{
//					placementPoolWin = (firstPlaceWin+secondPlaceWin+thirdPlaceWin)/3;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()==4)
//				{
//					placementPoolWin = (firstPlaceWin+secondPlaceWin+thirdPlaceWin+fourthPlaceWin)/4;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()>=5)
//				{
//					placementPoolWin = (firstPlaceWin+secondPlaceWin+thirdPlaceWin+fourthPlaceWin+fifthPlaceWin)/winSummary.getNumberOfPeopleSplitWith();
//				}
//				else
//				{
//					placementPoolWin = firstPlaceWin;
//				}
//			}
//			else if (winSummary.getPlace()==2&&league.getSecondPlacePercent()!=0) 
//			{
//				if (winSummary.getNumberOfPeopleSplitWith()==2)
//				{
//					placementPoolWin = (secondPlaceWin+thirdPlaceWin)/2;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()==3)
//				{
//					placementPoolWin = (secondPlaceWin+thirdPlaceWin+fourthPlaceWin)/3;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()>=4)
//				{
//					placementPoolWin = (secondPlaceWin+thirdPlaceWin+fourthPlaceWin+fifthPlaceWin)/winSummary.getNumberOfPeopleSplitWith();
//				}
//				else
//				{
//					placementPoolWin = secondPlaceWin;
//				} 
//			}
//			else if (winSummary.getPlace()==3&&league.getThirdPlacePercent()!=0)
//			{
//				if (winSummary.getNumberOfPeopleSplitWith()==2)
//				{
//					placementPoolWin = (thirdPlaceWin+fourthPlaceWin)/2;
//				}
//				else if (winSummary.getNumberOfPeopleSplitWith()>=3)
//				{
//					placementPoolWin = (thirdPlaceWin+fourthPlaceWin+fifthPlaceWin)/winSummary.getNumberOfPeopleSplitWith();
//				}
//				else
//				{
//					placementPoolWin = thirdPlaceWin;
//				} 
//			}
//			else if (winSummary.getPlace()==4&&league.getFourthPlacePercent()!=0)
//			{
//				if (winSummary.getNumberOfPeopleSplitWith()>=2)
//				{
//					placementPoolWin = (fourthPlaceWin+fifthPlaceWin)/winSummary.getNumberOfPeopleSplitWith();
//				}
//				else
//				{
//					placementPoolWin = fourthPlaceWin;
//				} 
//			}
//			else if (winSummary.getPlace()==5&&league.getFifthPlacePercent()!=0)
//			{
//				if (winSummary.getNumberOfPeopleSplitWith()>=2)
//				{
//					placementPoolWin = fifthPlaceWin/winSummary.getNumberOfPeopleSplitWith();
//				}
//				else
//				{
//					placementPoolWin = fifthPlaceWin;
//				}
//				 
//			}
//			
//			
//			
////			int numberOfWeeksWon = winSummary.getNumberOfWins();
////			if (numberOfWeeksWon!=0)
////			{
////				placementPoolWin += numberOfWeeksWon * weeklyWin;
////			}
//			winSummary.setEntryPrizeWon(placementPoolWin);
//			
//			Map<Integer, WeekWinner> weekWins = winSummary.getWeekTotal();
//			List<Integer> weeks = new ArrayList<Integer>(weekWins.keySet());
//			Collections.sort(weeks);
//			int numberOfPushWeeks=0;
//			double kitty=0;
//			for (Integer week: weeks)
//			{
//				WeekWinner weekWinner = weekWins.get(week);
//				int splitsForWeek = weekSplits.get(week);
//				double moneyToBeSplit = (weeklyWin/weekSplits.get(week))/2;
//				if (splitsForWeek>1)
//				{
//					numberOfPushWeeks++; 
//					kitty += moneyToBeSplit;
//				}
//				if (weekWinner.isWinner())
//				{
//					if (league.getDoubleType() == PickemTieBreakerEnum.SPLIT.getType())
//						winSummary.addWeekMoney(weeklyWin/weekSplits.get(week));
//					else if (league.getDoubleType() == PickemTieBreakerEnum.FIFTY_FIFTY_SPLIT.getType())
//					{
//						
//						if (splitsForWeek>1)
//						{
//							if (week==17)
//							{
//								double moneyWon = kitty+(weeklyWin/splitsForWeek);
//								winSummary.addWeekMoney(moneyWon);
//							}
//							else
//							{
//								winSummary.addWeekMoney(moneyToBeSplit);
//							}
//						}
//						else if (splitsForWeek==1)
//						{
//							winSummary.addWeekMoney(kitty);
//							winSummary.addWeekMoney(weeklyWin);
////							kitty=0;
//							numberOfPushWeeks=0;
//						}
//					}
//					else if (league.getDoubleType() == PickemTieBreakerEnum.PUSH_TO_NEXT_WEEK.getType())
//					{
////						if (splitsForWeek>1)
////						{
////							kitty+=weeklyWin;
////						}
////						else 
//						if (splitsForWeek==1)
//						{
//							double moneyWon = (weeklyWin*numberOfPushWeeks)+weeklyWin;
//							winSummary.addWeekMoney(moneyWon);
//							numberOfPushWeeks=0;
////							kitty=0;
//						}
//						else if (week==17&&numberOfPushWeeks>1)
//						{
//							double moneyWon = ((weeklyWin*numberOfPushWeeks))/splitsForWeek;
//							winSummary.addWeekMoney(moneyWon);
//							numberOfPushWeeks=0;
//						}
//					}
//					else if (league.getDoubleType() == PickemTieBreakerEnum.MONDAY_NIGHT_SCORE.getType())	
//					{
//						
//					}
//					weekWinner.setTiesForWeek(weekSplits.get(week));
//				}
//				if (splitsForWeek==1)
//				{
//					numberOfPushWeeks=0;
//					kitty=0;
//				}
//			}
//			placementPoolWin=0;
//		}
//	}
}
