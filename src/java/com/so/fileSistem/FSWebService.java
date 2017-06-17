/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.so.fileSistem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author jruiz
 */
@WebService(serviceName = "FSWebService")
public class FSWebService {
    
    ArrayList<Node> arbol = new ArrayList<Node>();
    
    ArrayList<String> ruta = new ArrayList<String>();
    
    
    private int createPath(String nombre){
        ruta.add(nombre);
        return ruta.size()-1;
    }
    private void addPath(int pos, String nombre){
       ruta.set(pos, ruta.get(pos).concat("-"+nombre));
    }
    
    private void setPath(int pos, String nombre){
       ruta.set(pos, nombre);
    }

    private void deletePath (int pos){
        String[] rutas = ruta.get(pos).split("-");
        String nuevaRuta = "";
        nuevaRuta +=  rutas[0];
        for (int i = 1; i < rutas.length -1 ; i++) {
            nuevaRuta += "-" + rutas[i];
        }
        
    }
   
   
   private String getPath(int pos){
       return ruta.get(pos);
   }
    
    private int crearRaizCliente(String nombre) {
       arbol.add(new Node(nombre));
       return arbol.size() - 1;
    }
    
    private Node getClientRoot(int pos){
        return arbol.get(pos);
    }
    
    private Node findRouteNode (int pos){
        String rutaActual[] =  getPath(pos).split("-");
        Node padre = getClientRoot(pos);
        System.out.println(padre.getId() + "vs" + rutaActual[0]);
        
        if(padre.getId().equals(rutaActual[0])){
            if(rutaActual.length>1){
                
                
                for (int i = 1; i < rutaActual.length; i++) {
                    List<Node> hijos = padre.getChildren();
                    System.out.println("padre = "+padre.getId());
                    for (int j = 0; j < hijos.size(); j++) {
                        
                        if(hijos.get(j).getId().equals(rutaActual[i])){
                            return hijos.get(j);
                        }
                    }
                }
                
                
            }else{
                return padre;
            }
        }else{
            System.out.println("SON DIFERENTES");
        }
        return null;
    }
    
 
    /************OPERACIONES*/
    
    
    //se crea el disco
    @WebMethod(operationName = "CRT")
    public int CRT(int sectores,int tamsec, String nombreraiz) throws IOException, Exception {
        
        String ruta = "C:\\Users\\jruiz\\Desktop\\"+nombreraiz + ".txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        
        //crea el archivo
        bw = new BufferedWriter(new FileWriter(archivo));

        while(sectores > 0 ){
            int tamano = tamsec;
            while(tamano > 0 ){
                bw.write("0");
                tamano--;
            }
            bw.write("-");
            sectores--;
        }

        bw.close();
        int posicion = crearRaizCliente(nombreraiz);
        int posicionPath = createPath(nombreraiz);
        if(posicion != posicionPath){
            throw  new Exception();
        }
        return posicion;
    }
    
    
    
    
    @WebMethod(operationName = "FLE")
    public void FLE(int pos, String contenido, String nombre, String extension){
       Node parent = findRouteNode(pos);
       Node archivo = new Node(nombre);
       parent.addChild(archivo);
       archivo.setExtension(extension);
       archivo.setFile(true);
       //TODO agregar contenido
    }
    
    @WebMethod(operationName = "MKDIR")
    public void MKDIR(int pos, String nombredirectorio){
       Node parent = findRouteNode(pos);
       Node dir = new Node(nombredirectorio);
       if(parent == null){
           System.out.println("NULO");
       }else{
           parent.addChild(dir);
       }
       //parent.addChild(dir);
    }
    
    @WebMethod(operationName = "CHDIR")
    public void CHDIR(int pos, String direccion){
        setPath(pos, direccion);
    }
    
    @WebMethod(operationName = "LDIR")
    public String LDIR(int pos){
        return findRouteNode(pos).printTree("-");
    }
    
    @WebMethod(operationName = "MFLE")
    public void MFLE(String nombrearchivo, String nuevocontenido){
    
    }
    
    
    @WebMethod(operationName = "PPT")
    public String PPT(int pos, String nombrearchivo){
        addPath(pos, nombrearchivo);
        System.out.println(getPath(pos));
        String props = findRouteNode(pos).propiedades();
        System.out.println(getPath(pos));
        deletePath(pos);
        System.out.println(getPath(pos));
        return props;
    }
    
    @WebMethod(operationName = "VIEW")
    public void VIEW(String nombrearchivo){
    
    }
    
     @WebMethod(operationName = "CPY")
    public void CPY(String nombrearchivo){
    
    }
   
    @WebMethod(operationName = "MOV")
    public void MOV(String nombrearchivo, String nuevaruta){
    
    }
    
    @WebMethod(operationName = "REM")
    public void REM(String nombrearchivo){
    
    }
    
     @WebMethod(operationName = "TREE")
    public String TREE(int pos){
        return getClientRoot(pos).printTree("-");
    }
    
    /*********/
    
    
    @WebMethod(operationName = "square")
    public double square(double num) {
        return num-num;
    }
    
    
    
}