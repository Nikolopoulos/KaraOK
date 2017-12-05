/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shark.tornado;

/**
 *
 * @author Basil
 */
public class lyric {
    
    long time;
    String s;
    int positionInText;

  public int getPositionInText() {
    return positionInText;
  }

  public void setPositionInText(int positionInText) {
    this.positionInText = positionInText;
  }

    public lyric(long time, String s) {
        this.time = time;
        this.s = s;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return (s+"\t          "+time);
    }
    
    
}
