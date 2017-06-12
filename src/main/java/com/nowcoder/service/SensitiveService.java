package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LZF on 2017/6/12.
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private class TrieNode{
        //该结点是否是某个关键词的结尾
        private boolean end = false;
        //当前结点下所有的子结点     某个结点存在的所有子结点
        private Map<Character, TrieNode> subNodes = new HashMap<Character,TrieNode>();

        //获取指定子结点
        public TrieNode getTrieNode(Character key){
            return subNodes.get(key);
        }

        //添加子结点(key,trieNode)
        public void addSubNode(Character key, TrieNode trieNode){
            subNodes.put(key,trieNode);
        }

        public boolean isKeywordEnd(){
            return end;
        }

        public void setKeywordEnd(boolean end){
            this.end = end;
        }
    }
    //根节点
    private TrieNode rootNode = new TrieNode();

    //增加敏感词     构造字典树   比如添加敏感词abc
    private void addWord(String lineTxt){
        TrieNode tmpNode = rootNode;
        for(int i = 0; i < lineTxt.length(); i++){
            Character c = lineTxt.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TrieNode node = tmpNode.getTrieNode(c);
            if(node == null){//不存在该字符  进行添加该字符结点
                node = new TrieNode();
                tmpNode.addSubNode(c,node);
            }
            tmpNode = node;//指向下一个结点

            if(i == lineTxt.length() - 1){
                //关键词结束，设置true结束标志
                tmpNode.setKeywordEnd(true);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        try{//Thread.currentThread()返回对当前正在执行的线程对象的引用  getContextClassLoader()返回该线程的上下文 ClassLoader
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");//注意"SensitiveWords.txt"目录在resources目录下
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine()) != null){
                lineTxt = lineTxt.trim();//去除字符串前后空格
                addWord(lineTxt);
            }
            read.close();
        }catch (Exception e){
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    /**
     * 判断是否是一个符号(非英文、非数字、非中文)
     */
    private boolean isSymbol(char c){
        int ic  = (int) c;
        //CharUtils.isAsciiAlphanumeric(c)判断c是否是字母(英文)或者数字  是返回true    0x2E80-0x9FFF 东亚文字范围  ！的优先级大于&&
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //过滤敏感词
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        StringBuilder result =new StringBuilder();
        String replacement = "***";
        TrieNode tmpNode = rootNode;
        int begin = 0;
        int position = 0;
        while(position < text.length()){
            Character c = text.charAt(position);

            if(isSymbol(c)){
                if(tmpNode == rootNode){//不包括在敏感词中的符号保留
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
             tmpNode = tmpNode.getTrieNode(c);
             if(tmpNode == null){
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tmpNode = rootNode;
            }else if(tmpNode.isKeywordEnd()){//发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tmpNode = rootNode;
            }else{
                ++position;
            }
        }
        result.append(text.substring(begin));//text.substring(begin)中的begin的大小不能超过text的长度 即text.length()
        return result.toString();
    }

    public static void main(String[] args){
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        sensitiveService.addWord("赌博");
        System.out.println(sensitiveService.filter("hi 色￥￥~~~情"));
    }
}
