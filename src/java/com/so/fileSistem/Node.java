/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.so.fileSistem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kimco
 */
class Node {
     private String id;
    private final List<Node> children = new ArrayList<>();
    private Node parent = null;
    private boolean isFile = false;
    private String extension = "";
    private int lenght = 0;
    private int position = 0;    
    private Date fechaCreacion = null;
    private Date fechaModificacion = null;
    
    

    public Node(String id) {
        this.id = id;
        fechaCreacion = new Date();
        fechaModificacion = fechaCreacion;
    }
    
    private void updateDate(){
        fechaModificacion = new Date();
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
        updateDate();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        updateDate();
    }
    
    
    
    public String getId() {
     return id;
    }

    public void setId(String id) {
     this.id = id;
     updateDate();
    }
    
    public String getExtension() {
     return extension;
    }

    public void setExtension(String extension) {
     this.extension = extension;
     updateDate();
    } 
    
    public boolean isFile() {
     return isFile;
    }

    public void setFile(boolean isFile) {
     this.isFile = isFile;
    }

    public List<Node> getChildren() {
     return children;
    }
    
    public void setParent(Node pParent){
        this.parent = pParent;
        updateDate();
    }

    public Node getParent() {
     return parent;
    }
    
    public void addChild(Node child){
        child.setParent(this);
        this.getChildren().add(child);
        updateDate();
    }
    
    public String printTree(String separator){
        StringBuilder treeString = new StringBuilder(); 
        treeString.append(separator);
        treeString.append(">");
        if(this.isFile){
            treeString.append('*');
        }
        treeString.append(this.getId());
        if(this.isFile){
            treeString.append(this.getExtension());
            treeString.append('*');
        }
        treeString.append("\n");
        this.getChildren().forEach((child) -> {
            treeString.append(child.printTree("  |"+separator));
         });
        return treeString.toString();
    }
    
    public String propiedades(){
        String propiedades =  "Nombre: "+ this.getId()+ '\n';
        if(this.isFile){
            propiedades += "Extensión: " + this.getExtension()+ '\n';
        }
        propiedades += "Fecha Creacion: " + this.fechaCreacion +'\n' + 
                "Fecha Modificación: "+ this.fechaModificacion + '\n' +
                "Tamaño " + this.lenght + '\n';
        return propiedades;
    }
    
}