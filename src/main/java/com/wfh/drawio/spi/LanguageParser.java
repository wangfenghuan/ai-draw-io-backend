package com.wfh.drawio.spi;


import com.wfh.drawio.core.model.ProjectAnalysisResult;

import java.io.File;

/**
 * 核心解析器接口
 * 贡献者只需要实现这个接口，就能支持一种新语言
 */
public interface LanguageParser {

    /**
     * 1. 这个解析器叫什么名字？
     * e.g., "Java-Spring", "Python-Django", "MySQL-DDL"
     */
    String getName();

    /**
     * 2. 这个解析器支持这种文件/项目吗？
     * @param projectDir 用户上传的项目根目录或文件
     * @return true 如果支持
     */
    boolean canParse(String projectDir);

    /**
     * 3. 核心逻辑：把代码变成通用模型
     * @param projectDir 项目文件
     * @return 分析结果 (节点+关系)
     */
    ProjectAnalysisResult parse(String projectDir);
}