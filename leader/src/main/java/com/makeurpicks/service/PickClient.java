package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.DoublePickView;
import com.makeurpicks.domain.PickView;

@FeignClient("pick")
public interface PickClient {

	@RequestMapping(method=RequestMethod.GET, value="/picks/leagueid/{leagueid}/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody Iterable<PickView> getPicksByLeaguenWeekAndPlayer(@PathVariable String leagueid, @PathVariable String weekid, @PathVariable String playerid);
	
	@RequestMapping(method=RequestMethod.GET, value="/picks/double/leagueid/{leagueid}/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody DoublePickView getDoublePick(@PathVariable String leagueid, @PathVariable String weekid, @PathVariable String playerid);
	
}
