package com.swopt.swoptping.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="AppConfig")
public class AppConfig {
	private String rstaTheme,locale;
	private List<String> hosts;
	
	@XmlElement(name="RstaTheme")
	public String getRstaTheme() {
		return rstaTheme;
	}

	public void setRstaTheme(String rstaTheme) {
		this.rstaTheme = rstaTheme;
	}
	
	@XmlElement(name="Locale")
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@XmlElementWrapper(name="Hosts")
	@XmlElement(name="Host")
	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

}
