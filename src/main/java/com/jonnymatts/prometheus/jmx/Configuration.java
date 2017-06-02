package com.jonnymatts.prometheus.jmx;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Configuration {
    public static final Duration DEFAULT_SCRAPE_INTERVAL = Duration.of(15, SECONDS);

    @JsonProperty("scrapeIntervalInSeconds")
    private Duration scrapeInterval;
    private List<Bean> beans;

    public Configuration() {
        this.scrapeInterval = DEFAULT_SCRAPE_INTERVAL;
    }

    public Configuration(Duration scrapeInterval, List<Bean> beans) {
        this.scrapeInterval = scrapeInterval;
        this.beans = beans;
    }

    public Duration getScrapeInterval() {
        return scrapeInterval;
    }

    public void setScrapeInterval(Duration scrapeInterval) {
        if(scrapeInterval != null)
            this.scrapeInterval = scrapeInterval;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (beans != null ? !beans.equals(that.beans) : that.beans != null) return false;
        return scrapeInterval != null ? scrapeInterval.equals(that.scrapeInterval) : that.scrapeInterval == null;
    }

    @Override
    public int hashCode() {
        int result = beans != null ? beans.hashCode() : 0;
        result = 31 * result + (scrapeInterval != null ? scrapeInterval.hashCode() : 0);
        return result;
    }
}