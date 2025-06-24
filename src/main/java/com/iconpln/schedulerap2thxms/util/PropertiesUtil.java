package com.iconpln.schedulerap2thxms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    @Value("${app.page}")
    public int page;

    @Value("${app.size}")
    public int size;

    @Value("${app.from}")
    public String from;

    @Value("${app.to}")
    public String to;

    @Value("${app.flagged}")
    public boolean flagged;
}
