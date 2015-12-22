package com.makeurpicks.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.WeekStats;
import com.makeurpicks.domain.WinSummary;

@RestController
@RequestMapping(value="/leaders")
public class LeaderController {

	@RequestMapping(value="/seasonid/{seasonid}/weekid/{weekid}")
	public @ResponseBody WeekStats getPlayersPlusWinsInLeague(@PathVariable String seasonid, @PathVariable String weekid)
	{
		return null;
	}
	
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
