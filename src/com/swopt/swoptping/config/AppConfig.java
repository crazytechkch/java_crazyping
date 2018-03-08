package com.swopt.swoptping.config;

import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="AppConfig")
public class AppConfig {
	private String rstaTheme,locale;
	private Integer alwaysOnTop;
	private Integer width, height;
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
		if(locale==null) {
			return Locale.getDefault().getLanguage();
		}
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
	
	@XmlElement(name="AlwaysOnTop")
	public Integer getAlwaysOnTop() {
		return alwaysOnTop;
	}

	public void setAlwaysOnTop(Integer alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}
	
	@XmlElement(name="DimenWidth")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@XmlElement(name="DimenHeight")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

}
