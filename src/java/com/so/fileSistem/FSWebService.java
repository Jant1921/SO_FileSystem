/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.so.fileSistem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author jruiz
 */
@WebService(serviceName = "FSWebService")
public class FSWebService {
    final String DISK_PATH = "C:\\Users\\jruiz\\Desktop\\";
    ArrayList<Node> arbol;
    
    ArrayList<String> ruta;

    public FSWebService() {
        this.arbol = new ArrayList<>();
        this.ruta = new ArrayList<>();
    }
    
    
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
        ruta.set(pos,nuevaRuta);
        
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
                
                
                List<Node> hijos = padre.getChildren();
                String nombreNodoABuscar = rutaActual[1];
                for (int i = 0; i < hijos.size(); i++) {
                    Node hijoActual = hijos.get(i);
                    if(hijoActual.getId().equals(nombreNodoABuscar)){
                        return findNodeHelper(removeFirstDirPath(rutaActual),hijoActual);
                    }
                }
                return null;
                
            }else{
                return padre;
            }
        }else{
            System.out.println("SON DIFERENTES");
        }
        return null;
    }
    private String removeFirstDirPath(String[] pRuta){
        String nuevaRuta = "";
        nuevaRuta += pRuta[1];
        for (int i = 2; i < pRuta.length; i++) {
            nuevaRuta += "-"+pRuta[i];
        }
        return nuevaRuta;
    }
    
    private Node findNodeHelper(String pRuta,Node nodoActual){
        String rutaActual[] =  pRuta.split("-");
         if(nodoActual.getId().equals(rutaActual[0])){
            if(rutaActual.length>1){
                List<Node> hijos = nodoActual.getChildren();
                String nombreNodoABuscar = rutaActual[1];
                for (int i = 0; i < hijos.size(); i++) {
                    Node hijoActual = hijos.get(i);
                    if(hijoActual.getId().equals(nombreNodoABuscar)){
                        return findNodeHelper(removeFirstDirPath(rutaActual),hijoActual);
                    }
                }
                return null;           
            }else{
                return nodoActual;
            }
        }else{
            System.out.println("SON DIFERENTES");
            return null;
        }
        
    }
    
    
        
    private int bestFit(int pos, int tamanoContenido){
        File file = new File("C:\\Users\\jruiz\\Desktop\\"+ getClientRoot(pos).getId()+ ".txt");
        RandomAccessFile access;
        String disk;
        try {
            access = new RandomAccessFile(file, "rw");
            disk = access.readLine();
            
            String diskSegments[] = disk.split("-");
            int totalSegmentos = diskSegments.length;
            int tamanoSegmento = diskSegments[0].length();
            
            
            String segmentoVacio = "";
            for (int i = 0; i < tamanoSegmento; i++) {
                segmentoVacio += "0";
            }
            int segmentosVacios = 0;
            
            int bestSegment = -1;
            int bestSize = 999;
            int candidato = 0;
            int tamanoCandidato = 999;
            
            for (int i = 0; i < diskSegments.length ; i++) {
                if(diskSegments[i].equals(segmentoVacio)){
                    if(segmentosVacios == 0){
                        candidato = i;
                    }
                    segmentosVacios += 1;
                }else{
                    tamanoCandidato = segmentosVacios * tamanoSegmento;
                    if(tamanoCandidato < bestSize && tamanoCandidato > tamanoContenido){
                        bestSize = tamanoCandidato;
                        bestSegment = candidato;
                    }
                    segmentosVacios = 0;
                }
            }
            tamanoCandidato = segmentosVacios * tamanoSegmento;
            if(tamanoCandidato < bestSize && tamanoCandidato > tamanoContenido){
                bestSize = tamanoCandidato;
                bestSegment = candidato;
            }         
            
            
            access.close();
     
             if(bestSegment != -1){
                return bestSegment + 1;
            }
            return -1;
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return -1;
        } catch (IOException ex) {
            System.out.println(ex);
            return -1;
        }
    }
    
    private boolean writeDisk(int pos,int segmentoActual, String contenido){
        
        File file = new File("C:\\Users\\jruiz\\Desktop\\"+ getClientRoot(pos).getId()+ ".txt");
        RandomAccessFile access;
        String disk;
        try {
            access = new RandomAccessFile(file, "rw");
            disk = access.readLine();
            
            String diskSegments[] = disk.split("-");
            int totalSegmentos = diskSegments.length;
            int tamanoSegmento = diskSegments[0].length();
            int segmentosRestantes = totalSegmentos - (segmentoActual - 1);
            final int totalDisk = disk.length();


            int availableDisk = (segmentosRestantes * tamanoSegmento);

            int segmentOffset = ((segmentoActual-1) * tamanoSegmento) + (segmentoActual-1);
            int actualOffset = segmentOffset;
            int fileOffset = 0;
            
            if( contenido.length() <= availableDisk ){
                for (int i = 0; fileOffset < contenido.length(); i++) {
                
                actualOffset = segmentOffset + i;
                if(actualOffset < totalDisk ){
                    access.seek(actualOffset);
                    if(disk.charAt(actualOffset)!='-'){
                        access.writeBytes(""+contenido.charAt(fileOffset));
                        fileOffset++;
                    }
                }

                }
                access.close();
                return true;
            }else{
                access.close();
                return false;
            }        
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return false;
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
           
        
    }
    
    private String readFileContent(int pos, int fileSegment, int fileSize){
        File file = new File("C:\\Users\\jruiz\\Desktop\\"+ getClientRoot(pos).getId()+ ".txt");
        RandomAccessFile access;
        String disk;
        fileSegment--;
        try {
            access = new RandomAccessFile(file, "r");
            disk = access.readLine();
            
            String diskSegments[] = disk.split("-");
            int tamanoSegmento = diskSegments[0].length();
            String fileContent = "";
            int charsLeidos = 0;
            int segmentosLeidos = 0;
            int charSegment = 0;
            while(charsLeidos < fileSize){
                if(charSegment < tamanoSegmento){
                    fileContent += diskSegments[fileSegment+segmentosLeidos].charAt(charSegment);
                    charSegment++;
                    charsLeidos++;
                }else{
                    charSegment = 0;
                    segmentosLeidos++;
                    
                }
            }
            access.close();
            return fileContent;
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return null;
        } catch (IOException ex) {
            System.out.println(ex);
            return null;
        }
        
    }
    
 
    //************OPERACIONES*/
    
    
    //se crea el disco
    @WebMethod(operationName = "CRT")
    public int CRT(int sectores,int tamsec, String nombreraiz) throws IOException, Exception {
        
        String rutaFile = DISK_PATH +nombreraiz + ".txt";
        File archivo = new File(rutaFile);
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
    public String FLE(int pos, String contenido, String nombre, String extension){
       
        int bestFitSegment = bestFit(pos,contenido.length());
        if(bestFitSegment != -1 ){
            Node parent = findRouteNode(pos);
            Node archivo = new Node(nombre);
            parent.addChild(archivo);
            archivo.setExtension(extension);
            archivo.setFile(true);
            archivo.setLenght(contenido.length());
            archivo.setPosition(bestFitSegment);
            writeDisk(pos,bestFitSegment,contenido);
            return "Archivo creado";
        }else{
            return "No hay espacio en disco";
        }
        
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
        //ver si queda espacio en los sectores que tiene el file,
        //si no alcanza llamar a bestFit , luego a writeFile y de ultimo
        //borrar el contenido de la ubicacion anterior
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
    public String VIEW(int pos, String nombrearchivo){
        addPath(pos, nombrearchivo);
        System.out.println(getPath(pos));
        Node archivo = findRouteNode(pos);
        
        String content = readFileContent(pos,archivo.getPosition(),archivo.getLenght());
        deletePath(pos);
        return content;
    }
    
     @WebMethod(operationName = "CPY")
    public void CPY(String nombrearchivo){
        //virtual - real
        //real - virtual
        // virtual - vitual
        //?
    }
   
    @WebMethod(operationName = "MOV")
    public void MOV(String nombrearchivo, String nuevaruta){
        //mover el nodo nombrearchivo de un padre a otro
    }
    
    @WebMethod(operationName = "REM")
    public void REM(int pos, String nombrearchivo){
        Node parent = findRouteNode(pos);
        parent.deleteChild(nombrearchivo);
        //si child es un file, hay que borrar el contenido del disco
    }
    
     @WebMethod(operationName = "TREE")
    public String TREE(int pos){
        return getClientRoot(pos).printTree("-");
    }
    @WebMethod(operationName = "PATH")   
    public String path(int pos){
        return getPath(pos);
    }
    
    
}
