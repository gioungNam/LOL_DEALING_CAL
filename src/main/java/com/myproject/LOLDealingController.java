package com.myproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

@Controller
public class LOLDealingController {
	@GetMapping("/hello")
	public String hello(@RequestParam(value="name", defaultValue = "World") String name) 
	throws RiotApiException {
		ApiConfig config = new ApiConfig().setKey("RGAPI-0174a010-c5d1-494d-a983-75ac184073d0");
		RiotApi api = new RiotApi(config);
		
		Summoner summoner = api.getSummonerByName(Platform.KR, "oung");
		System.out.println("Name:" + summoner.getName());
		System.out.println("Summoner ID: "+summoner.getId());
		System.out.println("Account ID: "+summoner.getAccountId());
		System.out.println("PUUID: "+summoner.getPuuid());
		System.out.println("Summoner Level: "+summoner.getSummonerLevel());
		System.out.println("Profile Icon ID: "+summoner.getProfileIconId());
		
		return "champInfo";
	}
	
	@GetMapping("/champInfo")
	public String champInfo(Model model) throws RiotApiException {
		JSONParser parser = new JSONParser();
		
		try {
			//Ddragon 정보객체
			URL url = new URL("http://ddragon.leagueoflegends.com/cdn/10.9.1/data/ko_KR/champion/Fiddlesticks.json");
			BufferedReader champDdragon = new BufferedReader(new InputStreamReader(url.openStream()));
			JSONObject jsonObject = (JSONObject)parser.parse(champDdragon);
			JSONObject champInfo = (JSONObject)jsonObject.get("data");
			JSONObject fiddlesticks = (JSONObject)champInfo.get("Fiddlesticks");
			JSONArray fiddle_spells = (JSONArray)fiddlesticks.get("spells");
			
//			for (int i=0; i < fiddle_spells.size(); i++) {
//				System.out.println(fiddle_spells.get(i));
//			}
			
		
//			BufferedReader champCdragon = new BufferedReader(new InputStreamReader(url.openStream()));
			url = new URL("http://localhost:8080/static/json/fiddlesticks.json");
			BufferedReader champCdragon = new BufferedReader(new InputStreamReader(url.openStream()));
			JSONObject jsonObject2 = (JSONObject)parser.parse(champCdragon);
			JSONObject skillQ = (JSONObject)jsonObject2.get("{53b383c4}");
			JSONObject skillQSpell = (JSONObject)skillQ.get("mSpell");
			JSONArray skillQSpellDetail = (JSONArray)skillQSpell.get("mDataValues");
			
			for (int i=0; i<skillQSpellDetail.size(); i++)
			{
				JSONObject skillValue = (JSONObject)skillQSpellDetail.get(i);
//				System.out.println(skillValue.get("mName"));
				if ("FearDuration".equals(skillValue.get("mName"))) {
					List valueList = (JSONArray)skillValue.get("mValues");
					valueList = valueList.subList(1, valueList.size()-1);
					StringBuilder stringBuilder = new StringBuilder("");
					for (int j = 0; j < valueList.size() - 1; j++) {
						stringBuilder.append(valueList.get(j)+"/");
					}
					stringBuilder.append(valueList.get(valueList.size() - 1));
					System.out.println(stringBuilder);
					model.addAttribute("FearDuration".toLowerCase(), stringBuilder.toString());
				}
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "champInfo";
	}
}
