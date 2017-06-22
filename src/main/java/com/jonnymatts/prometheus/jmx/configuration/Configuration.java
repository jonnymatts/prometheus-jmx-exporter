package com.jonnymatts.prometheus.jmx.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonnymatts.prometheus.jmx.collectors.HistogramMetricConfiguration;
import com.jonnymatts.prometheus.jmx.collectors.SummaryMetricConfiguration;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Configuration {
    public static final Duration DEFAULT_SCRAPE_INTERVAL = Duration.of(15, SECONDS);

    @JsonProperty("scrapeIntervalInSeconds")
    private Duration scrapeInterval;
    private List<HistogramMetricConfiguration> histograms;
    private List<SummaryMetricConfiguration> summaries;
    private List<Bean> beans;

    public Configuration() {
        this.scrapeInterval = DEFAULT_SCRAPE_INTERVAL;
    }

    public Configuration(Duration scrapeInterval,
                         List<HistogramMetricConfiguration> histograms,
                         List<SummaryMetricConfiguration> summaries,
                         List<Bean> beans) {
        this.scrapeInterval = scrapeInterval;
        this.histograms = histograms;
        this.summaries = summaries;
        this.beans = beans;
    }

    public Duration getScrapeInterval() {
        return scrapeInterval;
    }

    public void setScrapeInterval(Duration scrapeInterval) {
        if(scrapeInterval != null)
            this.scrapeInterval = scrapeInterval;
    }

    public List<HistogramMetricConfiguration> getHistograms() {
        return histograms;
    }

    public void setHistograms(List<HistogramMetricConfiguration> histograms) {
        this.histograms = histograms;
    }

    public List<SummaryMetricConfiguration> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<SummaryMetricConfiguration> summaries) {
        this.summaries = summaries;
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

        if (scrapeInterval != null ? !scrapeInterval.equals(that.scrapeInterval) : that.scrapeInterval != null)
            return false;
        if (histograms != null ? !histograms.equals(that.histograms) : that.histograms != null) return false;
        if (summaries != null ? !summaries.equals(that.summaries) : that.summaries != null) return false;
        return beans != null ? beans.equals(that.beans) : that.beans == null;
    }

    @Override
    public int hashCode() {
        int result = scrapeInterval != null ? scrapeInterval.hashCode() : 0;
        result = 31 * result + (histograms != null ? histograms.hashCode() : 0);
        result = 31 * result + (summaries != null ? summaries.hashCode() : 0);
        result = 31 * result + (beans != null ? beans.hashCode() : 0);
        return result;
    }
}