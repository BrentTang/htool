package com.vimdream.htool.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vimdream.htool.object.ObjectUtil;
import com.vimdream.htool.string.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Title: JSON
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/8/25 13:46
 */
public class JSON {

    private String json;
    private JSONObject object;
    private JSONArray array;

    private int rootType = 0;

    public static final int JSON_STRING = 1;
    public static final int JSON_OBJECT = 2;
    public static final int JSON_ARRAY = 3;

    public static final String ARRAY_PREFIX = "[";
    public static final String ARRAY_PREFIX_SPLIT_REGEX = "\\[";
    public static final String ARRAY_SUFFIX = "]";
    public static final char PATH_SEPARATOR = '.';
    public static final String PATH_SPLIT_REGEX = "\\.";

    public JSON(String json) {
        this.json = json;
        this.rootType = JSON_STRING;
    }

    public JSON(JSONObject object) {
        this.object = object;
        this.rootType = JSON_OBJECT;
    }

    public JSON(JSONArray array) {
        this.array = array;
        this.rootType = JSON_ARRAY;
    }

    public Object get(String path) {
        try {
            return access(path);
        } catch (Exception e) {
            return null;
        }
    }

    public Object getOrDefault(String path, Object defaultVal) {
        return Optional.ofNullable(get(path)).orElse(defaultVal);
    }

    public <T> T get(String path, Class<T> clazz) {
        return ObjectUtil.convert(get(path), clazz);
    }

    public <T> T getOrDefault(String path, Object defaultVal, Class<T> clazz) {
        return ObjectUtil.convert(getOrDefault(path, defaultVal), clazz);
    }

    /**
     * 通过path访问val
     *  l2.l2_1[0].[3].[0]
     *  l2.l2_1[0].result[0]
     * @param path
     * @return
     */
    public Object access(String path) {

        // 解析path
        String correctPath = checkPath(path);
        String[] nodes = parsePath(correctPath);
        int len = nodes.length;

        // 检查root
        ensureRoot();

        // 获取结果
        int curType = this.rootType;
        JSONObject curObj = this.object;
        JSONArray curArr = this.array;
        int nextType = -1;
        String cur = "";
        String nextKey = "";
        for (int i = 0; i < len - 1; i++) {
            cur = nodes[i];
            nextType = getNodePathType(cur);
            nextKey = nextType == JSON_OBJECT ? cur : getArrayPathKey(cur);
            switch (curType) {
                case JSON_OBJECT:
                    if (curObj.containsKey(nextKey)) {
                        if (nextType == JSON_ARRAY) {
                            JSONArray array = getJSONArray(curObj, nextKey);
                            int index = getArrayPathIndex(cur);
                            if (i + 1 < len) {
                                if (index >= array.size()) {
                                    throw new IllegalArgumentException("数组下标越界 " + cur);
                                }
                                int type = getNodePathType(nodes[i + 1]);
                                if (type == JSON_ARRAY && nodes[i + 1].indexOf(ARRAY_PREFIX) == 0) {
                                    curArr = array.getJSONArray(index);
                                    curType = JSON_ARRAY;
                                } else {
                                    curObj = array.getJSONObject(index);
                                    curType = JSON_OBJECT;
                                }
                            }
                        } else {
                            curObj = getJSONObject(curObj, nextKey);
                        }
                    } else {
                        throw new IllegalArgumentException("不存在的key " + nextKey);
                    }
                    break;
                case JSON_ARRAY:
                    int index = getArrayPathIndex(cur);
                    if (index < curArr.size()) {
                        if (nextType == JSON_ARRAY) {
                            if (i + 1 < len) {
                                int type = getNodePathType(nodes[i + 1]);
                                if (type == JSON_ARRAY && nodes[i + 1].indexOf(ARRAY_PREFIX) == 0) {
                                    curArr = curArr.getJSONArray(index);
                                    curType = JSON_ARRAY;
                                } else {
                                    curObj = curArr.getJSONObject(index);
                                    curType = JSON_OBJECT;
                                }
                            }
                        } else {
                            curObj = curArr.getJSONObject(index);
                            curType = JSON_OBJECT;
                        }
                    } else {
                        throw new IllegalArgumentException("数组下标越界 " + cur);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("未知的类型");
            }
        }

        String lastNode = nodes[len - 1];
        int lastNodeType = getNodePathType(lastNode);
        String lastNodeKey = lastNodeType == JSON_OBJECT ? lastNode : getArrayPathKey(lastNode);
        switch (lastNodeType) {
            case JSON_OBJECT:
                return curObj.get(lastNodeKey);
            case JSON_ARRAY:
                int index = getArrayPathIndex(lastNode);
                if (curType == JSON_OBJECT) {
                    curArr = getJSONArray(curObj, lastNodeKey);
                }

                if (index < curArr.size()) {
                    return curArr.get(index);
                } else {
                    throw new IllegalArgumentException("数组下标越界 " + lastNode);
                }
            default:
                throw new IllegalArgumentException("未知的类型");
        }
    }

    private JSONArray getJSONArray(JSONObject obj, Object key) {
        Object o = obj.get(key);
        if (o == null) {
            throw new IllegalArgumentException("不存在的key " + key);
        }
        if (!(o instanceof JSONArray)) {
            throw new IllegalArgumentException(key + "对应的值不是JSONArray类型");
        }
        return (JSONArray) o;
    }

    private JSONObject getJSONObject(JSONObject obj, Object key) {
        Object o = obj.get(key);
        if (o == null) {
            throw new IllegalArgumentException("不存在的key " + key);
        }
        if (!(o instanceof JSONObject)) {
            throw new IllegalArgumentException(key + "对应的值不是JSONObject类型");
        }
        return (JSONObject) o;
    }

    /**
     * 获取节点类型
     * @param node
     * @return
     */
    private int getNodePathType(String node) {
        if (node.contains(ARRAY_PREFIX)) {
            return JSON_ARRAY;
        }
        return JSON_OBJECT;
    }

    /**
     * 获取数组的key   arr[1] => arr
     * @param node
     * @return
     */
    private String getArrayPathKey(String node) {
        return node.substring(0, node.indexOf(ARRAY_PREFIX));
    }

    /**
     * 获取数组索引   arr[1] => 1
     * @param node
     * @return
     */
    private int getArrayPathIndex(String node) {
        String index = node.substring(node.indexOf(ARRAY_PREFIX) + 1, node.length() - 1);
        return StringUtil.isBlank(index) ? 0 : Integer.parseInt(index);
    }

    /**
     * 检查路径是否有效
     * @param path
     * @return
     */
    private String checkPath(String path) {
        if (StringUtil.isBlank(path)) {
            throw new IllegalArgumentException("访问路径不能为空");
        }

        StringBuilder correctPath = new StringBuilder();
        int len = path.length();
        for (int i = 0; i < len; i++) {
            char c = path.charAt(i);
            if (c != ' ') {
                if (c == PATH_SEPARATOR && correctPath.charAt(correctPath.length() - 1) == PATH_SEPARATOR) {
                    throw new IllegalArgumentException("无效的path " + path);
                }
                correctPath.append(c);
            }
        }
        return correctPath.toString();
    }

    /**
     * 将path解析成 节点数组
     * @param path
     * @return
     */
    private String[] parsePath(String path) {

        String[] targets = path.split(PATH_SPLIT_REGEX);
        int len = targets.length;
        if (len < 1) {
            throw new IllegalArgumentException("无效的path " + path);
        }

        String[] nodes = correctNodes(targets);
        if (nodes == null) {
            throw new IllegalArgumentException("无效的path " + path);
        }
        return nodes;
    }

    /**
     * 规范节点   去掉空串 -> ""
     * @param targets
     * @return
     */
    private String[] correctNodes(String[] targets) {
        int left = 0;
        int right = targets.length - 1;
        while (left <= right && StringUtil.isBlank(targets[left])) {
            left++;
        }
        if (left > right) {
            return null;
        }

        while (right >= 0 && StringUtil.isBlank(targets[right])) {
            right--;
        }

        if (right - left < 2) {
            return Arrays.copyOfRange(targets, left, right + 1);
        }

        ArrayList<String> nodes = new ArrayList<>(right - left);
        for (int i = left; i < right; i++) {
            if (StringUtil.isBlank(targets[i])) {
                return null;
            }
            String node = targets[i];
            String[] ele = node.split(ARRAY_PREFIX_SPLIT_REGEX);
            if (ele.length > 1) {
                nodes.add(ele[0] + ARRAY_PREFIX + ele[1]);
                if (ele.length > 2) {
                    for (int j = 2; j < ele.length; j++) {
                        nodes.add(ARRAY_PREFIX + ele[j]);
                    }
                }
            }
            nodes.add(node);
        }
//        return Arrays.copyOfRange(targets, left, right + 1);
        return nodes.toArray(new String[nodes.size()]);
    }

    /**
     * 确保有JSON数据
     */
    private void ensureRoot() {

        switch (this.rootType) {
            case JSON_STRING:
                Object object = JSONObject.parse(this.json);
                if (object instanceof JSONObject) {
                    this.object = (JSONObject) object;
                    this.rootType = JSON_OBJECT;
                } else if (object instanceof JSONArray) {
                    this.array = (JSONArray) object;
                    this.rootType = JSON_ARRAY;
                } else {
                    throw new IllegalArgumentException("无效的JSON字符串");
                }
                break;
            case JSON_OBJECT:
                if (this.object == null) {
                    throw new IllegalArgumentException("JSONObject不能为空");
                }
                break;
            case JSON_ARRAY:
                if (this.array == null) {
                    throw new IllegalArgumentException("JSONArray不能为空");
                }
                break;
            default:
                throw new IllegalArgumentException("未发现JSON对象");
        }

    }
}
