package com.wfh.drawio.service;

import com.wfh.drawio.core.model.ProjectAnalysisResult;
import com.wfh.drawio.spi.LanguageParser;
import com.wfh.drawio.spi.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Title: SpringBootJavaParserService
 * @Author wangfenghuan
 * @Package com.wfh.drawio.service
 * @Date 2026/2/16
 * @description: Enhanced Java AST Parser Service with Spring Boot analysis
 */
@Service
@Slf4j
public class SpringBootJavaParserService {

    public ProjectAnalysisResult parseProject(String projectDir) {
        log.info("Parsing project directory: {}", projectDir);
        
        Optional<LanguageParser> parserOpt = ParserFactory.getParser(projectDir);
        
        if (parserOpt.isEmpty()) {
            throw new IllegalArgumentException("No suitable parser found for project: " + projectDir);
        }
        
        LanguageParser parser = parserOpt.get();
        log.info("Using parser: {}", parser.getName());
        
        return parser.parse(projectDir);
    }
}
