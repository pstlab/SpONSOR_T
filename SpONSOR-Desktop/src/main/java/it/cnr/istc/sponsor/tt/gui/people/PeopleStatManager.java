/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.gui.people;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it>
 */
public class PeopleStatManager {

    private static PeopleStatManager _instance = null;
    private List<String> circleMap = new ArrayList<>();
    private List<String> squareMap = new ArrayList<>();

    public static PeopleStatManager getInstance() {
        if (_instance == null) {
            _instance = new PeopleStatManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private PeopleStatManager() {
        super();
    }
    
    public void clear(){
        this.circleMap.clear();
        this.squareMap.clear();
    }
    
    public void addCircleCoords(int row, int column){
        circleMap.add(row+","+column);
    }
    
    public boolean isCircle(int row, int column){
        return circleMap.contains(row+","+column);
    }
    
    public void addSquareCoords(int row, int column){
        squareMap.add(row+","+column);
    }
    
    public boolean isSquare(int row, int column){
        return squareMap.contains(row+","+column);
    }

    
}
