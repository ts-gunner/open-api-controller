package com.forty.utils;

import cn.hutool.core.text.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathUtils {

    public static boolean isMatchPath(String[] patterns, String path) {
        return PathUtils.isMatchPath(Arrays.stream(patterns).collect(Collectors.toList()), path);
    }

    public static boolean isMatchPath(List<String> patterns, String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : patterns) {
            boolean match = matcher.match(pattern, path);
            if (match) return true;
        }
        return false;
    }

}
