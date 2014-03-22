<?php
	$resultCount=0;
	if ($_SERVER["REQUEST_METHOD"] == "GET")
	{
		$u="http://where.yahooapis.com/v1/";
		$appid="?appid=[Txs1FkLV34Fpu0PqQq8mpeHNQ7_UwKDFO3kQJfjYlS3jAwUvQkuSsEPm4_NDWpg5NQNvwFc-]";
		$ltype=$_GET["type"];
		$t="";
		if($ltype=="zip")
		{
			$t="concordance/usps/".$_GET["location"];
		}
		else
		{
			$t="places\$and(.q('".urlencode($_GET["location"])."'),.type(7));start=0;count=1";
		}
		$url=$u.$t.$appid;
		//echo $url;
		
		$ufeed=@file_get_contents($url);
		if($ufeed==false)
		{
			$xml = new SimpleXMLElement('<weather></weather>');
			$feed = $xml->addChild('feed','error');
			Header('Content-type: text/xml');
			echo $xml->asXML();
		}
		else
		{
			$xml=new SimpleXMLElement($ufeed);
						
			if($ltype=="zip")
			{
				$result=$xml->woeid;
			}
			else
			{
				$result=$xml->place->woeid;	
			}
			$resultCount=sizeof($result);
			if($resultCount!=0)
			{
				func($result);
			}
			else
			{
				$xml = new SimpleXMLElement('<weather></weather>');
				$feed = $xml->addChild('feed','error');
				Header('Content-type: text/xml');
				echo $xml->asXML();
			}
		}
	}
	function func($w)
	{
		$wurl="http://weather.yahooapis.com/forecastrss?w=".$w."&u=".$_GET["tempUnit"];
		$wfeed=@file_get_contents($wurl);
		if($wfeed!=false)
		{
			$wxml=new SimpleXMLElement($wfeed);
			
			$xml = new SimpleXMLElement('<weather></weather>');
			$feed = $xml->addChild('feed',$wurl);
			$link=$wxml->channel->link;
			if($link=="" || $link==" ")
				$link="N/A";
			$xml->addChild("link",$link);
			$namespaces=$wxml->getNameSpaces(true);	
			$yweather=$wxml->channel->item->children($namespaces['yweather']);
			$location=$wxml->channel->children($namespaces['yweather'])->location;
			$mylocation=$xml->addChild("location");

			$city=$location->attributes()->city;
			if($city=="")
				$city="N/A";
			$mylocation->addAttribute('city',$city);
			
			$region=$location->attributes()->region;
			if($region=="")
				$region="N/A";
			$mylocation->addAttribute('region',$region);

			$country=$location->attributes()->country;
			if($country=="")
				$country="N/A";
			$mylocation->addAttribute('country',$country);

			$units=$wxml->channel->children($namespaces['yweather'])->units->attributes()->temperature;
			$myunit=$xml->addChild("units");
			$myunit->addAttribute('temperature',$units);								

			$text=$yweather->condition->attributes()->text;
			$temp=$yweather->condition->attributes()->temp;
			$mycond=$xml->addChild("condition");
			$mycond->addAttribute('text',$text);
			$mycond->addAttribute('temp',$temp);

			$dc=$wxml->channel->item->description;
			$start=strpos($dc,"=")+2;
			$length=(strpos($dc,">")-$start)-2;
			$image=substr($dc,$start,$length);
			$xml->addChild("img",$image);

			foreach($wxml->channel->item->children($namespaces['yweather'])->forecast as $v)
			{
				$myforecast=$xml->addChild('forecast');
				$myforecast->addAttribute('day',$v->attributes()->day);
				$myforecast->addAttribute('low',$v->attributes()->low);
				$myforecast->addAttribute('high',$v->attributes()->high);
				$myforecast->addAttribute('text',$v->attributes()->text);
			}

			Header('Content-type: text/xml');
			echo $xml->asXML();
		}
		else
		{
			$xml = new SimpleXMLElement('<weather></weather>');
			$feed = $xml->addChild('feed','error');
			Header('Content-type: text/xml');
			echo $xml->asXML();
		}		
	}	
?>