package code;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import given.AbstractHashMap;
import given.HashEntry;

import javax.sound.sampled.Line;

/*
 * The file should contain the implementation of a hashmap with:
 * - Separate Chaining for collision handling
 * - Multiply-Add-Divide (MAD) for compression: (a*k+b) mod p
 * - Java's own linked lists for the secondary containers
 * - Resizing (to double its size) and rehashing when the load factor gets above a threshold
 *   Note that for this type of hashmap, load factor can be higher than 1
 * 
 * Some helper functions are provided to you. We suggest that you go over them.
 * 
 * You are not allowed to use any existing java data structures other than for the buckets (which have been 
 * created for you) and the keyset method
 */

public class HashMapSC<Key, Value> extends AbstractHashMap<Key, Value> {

  // The underlying array to hold hash entry Lists
  private LinkedList<HashEntry<Key, Value>>[] buckets;
  Set<Key> keyList =  new HashSet<Key>();
  // Note that the Linkedlists are still not initialized!
  @SuppressWarnings("unchecked")
  protected void resizeBuckets(int newSize) {
    // Update the capacity
    N = nextPrime(newSize);
    buckets = (LinkedList<HashEntry<Key, Value>>[]) Array.newInstance(LinkedList.class, N);
  }

  /*
   * ADD MORE FIELDS IF NEEDED
   * 
   */

  // The threshold of the load factor for resizing
  protected float criticalLoadFactor;

  /*
   * ADD A NESTED CLASS IF NEEDED
   * 
   */

  public int hashValue(Key key, int iter) {
    return hashValue(key);
  }

  public int hashValue(Key key) {
    // TODO: Implement the hashvalue computation with the MAD method. Will be almost
   int code =(a* Math.abs(key.hashCode()) +b) % P;
   return Math.abs(code % N);
  }

  // Default constructor
  public HashMapSC() {
    this(101);
  }

  public HashMapSC(int initSize) {
    // High criticalAlpha for representing average items in a secondary container
    this(initSize, 10f);
  }

  public HashMapSC(int initSize, float criticalAlpha) {
    N = initSize;
    criticalLoadFactor = criticalAlpha;
    resizeBuckets(N);
    
    // Set up the MAD compression and secondary hash parameters
    updateHashParams();

    /*
     * ADD MORE CODE IF NEEDED
     * 
     */
  }

  /*
   * ADD MORE METHODS IF NEEDED
   * 
   */

  @Override
  public Value get(Key k) {
    // TODO Auto-generated method stub
    if(k==null){
      return null;
    }
    int index = hashValue(k);
    if (buckets[index] != null) {
      LinkedList<HashEntry<Key, Value>> temp = buckets[index];
        for(HashEntry<Key, Value> entry: temp){
          if(entry.getKey().equals(k)){
            return entry.getValue();
          }
        }
          return null;
    }
    return null;
  }

  @Override
  public Value put(Key k, Value v) {
    // TODO Auto-generated method stub
    // Do not forget to resize if needed!
    // Note that the linked lists are not initialized!
    if(k==null){
      return null;
    }
    checkAndResize();
    int index = hashValue(k);
    if (buckets[index] != null) {
      for (HashEntry<Key, Value> entry : buckets[index]) {
        if (entry.getKey().equals(k)) {
          Value returned = entry.getValue();
          entry.setValue(v);
          return returned;
        }
      }
        buckets[index].add(new HashEntry<>(k,v));
        keyList.add(k);
        n++;
        return null;
    }else{
      buckets[index] = new LinkedList<HashEntry<Key,Value>>();
      buckets[index].add(new HashEntry<>(k,v));
      keyList.add(k);
      n++;
      return null;
    }
  }

  @Override
  public Value remove(Key k) {
    // TODO Auto-generated method stub
    if(k==null){
      return null;
    }
    int index = hashValue(k);
    if(buckets[index] == null){
      return null;
    }else{
      for (HashEntry<Key,Value> entry : buckets[index]){
        if (entry.getKey().equals(k)) {
          Value returned = entry.getValue();
          buckets[index].remove(entry);
          keyList.remove(k);
          n--;
          return returned;
        }
      }
      return null;
    }
  }

  @Override
  public Iterable<Key> keySet() {
    // TODO Auto-generated method stub
    return keyList;
  }

  /**
   * checkAndResize checks whether the current load factor is greater than the
   * specified critical load factor. If it is, the table size should be increased
   * to 2*N and recreate the hash table for the keys (rehashing). Do not forget to
   * re-calculate the hash parameters and do not forget to re-populate the new
   * array!
   */
  protected void checkAndResize() {
    if (loadFactor() > criticalLoadFactor) {
      // TODO: Fill this yourself
      LinkedList<HashEntry<Key, Value>>[] temp = buckets;
      resizeBuckets(2 * N);
      updateHashParams();
      keyList.clear();
      n = 0;
      for (LinkedList<HashEntry<Key, Value>> lists : temp) {
        if (lists != null) {
          for (HashEntry<Key, Value> entry : lists) {
            put(entry.getKey(), entry.getValue());
          }
        }
      }
    }
  }
}
