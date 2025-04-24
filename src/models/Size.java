package models;

public enum Size {
    SMALL(200), MEDIUM(300), LARGE(400);
    private final int volume;

    Size(int v) { 
        this.volume = v; 
    }
    
    public int getVolume() { 
        return volume; 
    }
}
