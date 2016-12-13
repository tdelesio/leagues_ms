package com.makeurpicks.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.makeurpicks.domain.ViewPickColumn;
import com.makeurpicks.domain.WinSummary;
import com.makeurpicks.service.LeaderService;

import rx.Observer;

@RestController
@RequestMapping(value="/leaders")
public class LeaderController {

	@Autowired 
	private LeaderService leaderService;
	
//	@RequestMapping(value="/week/leagueid/{leagueid}/weekid/{weekid}")
//	public @ResponseBody DeferredResult<List<List<ViewPickColumn>>> getPlayersPlusWinsInLeague(@PathVariable String leagueid, @PathVariable String weekid)
//	{
//		DeferredResult<List<List<ViewPickColumn>>> result = new DeferredResult<>();
//		leaderService.getPlayersPlusWinsInLeague(leagueid, weekid).subscribe(new Observer<List<List<ViewPickColumn>>>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//            }
//
//            @Override
//            public void onNext(List<List<ViewPickColumn>> weekStats) {
//                result.setResult(weekStats);
//            }
//        });
//        
//		return result;
//	
//	}
	
	@RequestMapping(value="/winsummary/seasonid/{seasonid}")
	public @ResponseBody List<WinSummary> getWinSummary(@PathVariable String seasonid)
	{
		return null;
	}
	
	@RequestMapping(value="/leagueid/{leagueid}")
	public @ResponseBody Map<Integer, String> getWeekWinners(@PathVariable String seasonid)
	{
		return null;
	}
	
	
}
