package com.patienttriage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for OpenAI API client.
 */
@Configuration
public class OpenAIConfig {

  private static final Logger logger = LoggerFactory.getLogger(OpenAIConfig.class);

  @Value("${openai.api.key}")
  private String apiKey;

  /**
   * Provides the OpenAI API key as a bean.
   * 
   * @return OpenAI API key string
   */
  @Bean
  public String openAIApiKey() {
    if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("${OPENAI_API_KEY}")) {
      logger.error("OpenAI API key is not configured. Please set OPENAI_API_KEY in .env file.");
      throw new IllegalArgumentException(
          "OpenAI API key is required. Please set OPENAI_API_KEY in .env file.");
    }
    
    logger.info("OpenAI API key loaded successfully");
    return apiKey;
  }
}

