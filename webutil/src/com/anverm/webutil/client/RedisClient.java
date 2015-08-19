/*******************************************************************************
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  There is no copyright registered for following awesome work.
 *  Whatever contributed here is not private but do no copy. Instead
 *  collaborate and spread knowledge. 
 *******************************************************************************/
package com.anverm.webutil.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.AsyncAppender;

import com.anverm.webutil.client.exception.RedisException;
import com.anverm.webutil.io.IOUtil;
import com.anverm.webutil.queue.RedisQueueReader;
import com.anverm.webutil.queue.RedisQueueWriter;
import com.anverm.webutil.queue.api.QueueReader;
import com.anverm.webutil.queue.api.QueueWriter;
import com.anverm.webutil.queue.exception.QueueException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @author anand
 * <a href="mailto:anand.vrm88@gmail.com">Anand Verma</a>$
 * Created on: 21-Nov-2014
 */
public class RedisClient {
  private static JedisPool jedisPool = null;
  private Jedis jedis = null;
  
  public RedisClient(String host, int port, int timeout, String password, RedisDB redisDB) {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, redisDB.getRedisDBNumber());
    jedis = jedisPool.getResource();
  }

  public enum RedisDB {
    ANALYTICS(0);
    
    private int redisDBNumber;
    RedisDB(int redisDBNumber){
      this.redisDBNumber = redisDBNumber;
    }
    public int getRedisDBNumber(){
      return redisDBNumber;
    }
  }
  
  static class Temp implements Serializable{
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
  
  public static void main(String[] args) throws IOException {
    String host = "localhost";
    int port = 6379;
    String password = "@nv$rMg@t$w@y";
    int timeout = 10000;


    Jedis jedis = null;

    QueueWriter queueWriter = null;
    QueueReader queueReader = null;
    try {


      /*
       * jedis = new Jedis("localhost", 6379); jedis.auth("@nv$rMg@t$w@y");
       * jedis.set("anverm-analytics-1", "say");
       */
      /*
       * initJedis(host, port, timeout, password, RedisDB.ANALYTICS); jedis = getJedis();
       * jedis.set("anverm-analytics-1", "say");
       */
      
      Temp t = new Temp();
      t.setName("now this gonna be awsume");
      System.out.println("before serialization: "+t.getName());
      
      queueWriter = new RedisQueueWriter("localhost", 6379, 5000, "@nv$rMg@t$w@y", RedisDB.ANALYTICS);
      //queueWriter.enqueue("temporary", t);
      t = null;
      
      //new AsyncAppender();

      queueReader = new RedisQueueReader("localhost", 6379, 5000, "@nv$rMg@t$w@y", RedisDB.ANALYTICS);
      System.out.println("blocking = "+queueReader.blockingDequeue("temporary", 1));
      Object z = queueReader.blockingDequeue("temporary");
      
/*      if(z instanceof Temp){
        System.out.println("Object type = "+z.getClass().getName());
      }
      System.out.println("after serialization = " + ((Temp)z).getName());*/
    } catch (QueueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (queueReader != null) {
        try {
          queueReader.close();
        } catch (QueueException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      if (queueWriter != null) {
        try {
          queueWriter.close();
        } catch (QueueException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
/*  private static void releaseJedis(Jedis jedis) {
    if (jedis != null) {
      jedisPool.returnResource(jedis);
    }
  }*/
  
  protected Object lpop(String key) throws RedisException {
    try {
      return convertBytesToObject(jedis.lpop(key.getBytes()));
    } catch (Exception e) {
      throw new RedisException(e);
    }
  }
  
  protected Object blpop(String key) throws RedisException {
    return this.blpop(key, 1);
  }
  
  protected Object blpop(String key, int timeout) throws RedisException {
    try {
      List<byte[]> obj = jedis.blpop(timeout, key.getBytes());
      if(obj == null){
        return null;
      }
      return convertBytesToObject(obj.get(1));
    } catch (Exception e) {
      throw new RedisException(e);
    }
  }
  
  protected boolean rpush(String key, Object obj) throws RedisException {
    try {
      return jedis.rpush(key.getBytes(),convertObjectToBytes(obj)) > 0;
    } catch (Exception e) {
      throw new RedisException(e);
    }
  }

  protected Set<String> getKeys(String pattern) throws RedisException {
    try {
      Set<byte[]> binaryKeys = jedis.keys(pattern.getBytes());
      Set<String> textKeys = new HashSet<String>();
      for (byte[] binaryKey : binaryKeys) {
        textKeys.add((String) convertBytesToObject(binaryKey));
      }
      return textKeys;
    } catch (Exception e) {
      throw new RedisException(e);
    }
  }
  
  protected void destroy() {
    if (jedis != null) {
      jedis.close();
    }
    if (jedisPool != null) {
      jedisPool.destroy();
    }
  }
 
  protected byte[] convertObjectToBytes(Object obj) throws IOException {
    if(obj == null){
      return null;
    }
    ByteArrayOutputStream baos = null;
    ObjectOutputStream os = null;
    try {
      baos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(baos);
      os.writeObject(obj);
      return baos.toByteArray();
    } finally {
      IOUtil.closeOutputStreamIgnoreException(os);
      IOUtil.closeOutputStreamIgnoreException(baos);
    }
  }
  
  protected Object convertBytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
    if(bytes == null){
      return null;
    }
    ByteArrayInputStream bais = null;
    ObjectInputStream is = null;
    try {
      bais = new ByteArrayInputStream(bytes);
      is = new ObjectInputStream(bais);
      return is.readObject();
    } finally {
      IOUtil.closeInputStreamIgnoreException(is);
      IOUtil.closeInputStreamIgnoreException(bais);
    }
  }
}
