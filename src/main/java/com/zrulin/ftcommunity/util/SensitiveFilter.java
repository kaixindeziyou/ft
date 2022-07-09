package com.zrulin.ftcommunity.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author zrulin
 * @create 2022-03-14 8:48
 */
@Component
public class SensitiveFilter {

    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //用来替代敏感词
    private static final String REPLACEMENT = "**";
    //前缀树的根节点
    private static final TrieNode rootNode = new TrieNode();

    // @PostConstruct 表明这是一个初始化方法，在类被实例化的时候就执行。
    @PostConstruct
    public void init(){
        // 获取类加载器，类加载器是从类路径下，去加载资源，所谓的类路径就是target/classes/目录之下。
        // 我们的程序一编译，所有的程序都编译到classes下，包括配置文件。
        try(
                //得到字节流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //从字节流中读文字不太方便，把字节流转成字符流，把字符流改成缓冲流会效率更高
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;  //用这个keyword去读数据
            while((keyword = reader.readLine()) != null){//一行一行的读
                //添加到前缀树
                this.addKeyWord(keyword);
            }
        }catch (IOException e){
            logger.error("获取铭感词文件失败："+e.getMessage());
        }
    }
    //把一个敏感词添加到前缀树中去。
    public void addKeyWord(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0 ; i< keyword.length() ; ++i){
            char key = keyword.charAt(i);
            //找子节点
            TrieNode subNode = tempNode.getSubNode(key);
            if(subNode == null){//如果没有这个子节点
                //初始化子节点；
                subNode = new TrieNode();
                tempNode.addSubNode(key,subNode);
            }
            //指向子节点，进入下一次循环
            tempNode = subNode;
            if(i == keyword.length() -1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return    过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1 ，指向树
        TrieNode tempNode = rootNode;
        //指针2 指向字符串的慢指针，一直往前走
        int begin = 0;
        //指针3 指向字符串的快指针，往后走检查，如果不是就归位。
        int position = 0;
        //结果
        StringBuilder result = new StringBuilder();
        while(position < text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //若指针处于根节点。则将此符号计入结果，让指针向下走一步。
                if(tempNode == rootNode){
                    result.append(c);
                    begin++;
                }
                //无论结构在开头或中间指针3都向下走一步。
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
//                以begin为开头的字符串不存在敏感词
                result.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                // 重新指向根节点。
                tempNode  = rootNode;
            }else if(tempNode.isKeyWordEnd()){
                //发现了敏感词,将begin-posttion中的字符串替换
                result.append(REPLACEMENT);
//                进入下一个位置。
                begin = ++ position;
                tempNode = rootNode;
            }else{
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结构
        result.append(text.substring(begin));
        return result.toString();
    }

    //判断是否为符号,是的话返回true，不是的话返回false
    public boolean isSymbol(Character c){
        //!CharUtils.isAsciiAlphanumeric(c)判断合法字符
        //c < 0x2E80 || c > 0x9fff 东亚文字的范围是0x2E80到0x9fff
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9fff);
    }

    //定义一个内部类，作为前缀树的结构
    private static class TrieNode{
        //关键词结束的标识
        private boolean isKeyWordEnd = false;
        //子节点(key 代表下级的节点字符， value是下级节点)
        private Map<Character, TrieNode> subNode = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character key,TrieNode subNode){
            this.subNode.put(key,subNode);
        }
        //获取子节点
        public TrieNode getSubNode(Character key){
            return subNode.get(key);
        }
    }
}
