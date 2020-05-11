package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao;
	private Map<Integer, Country> mappaStati;
	private Graph<Country, DefaultEdge> grafo;

	public Model() {

		dao = new BordersDAO();
		mappaStati = new HashMap<Integer, Country>();
	}

	public void createGraph(int anno) {
		grafo = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		dao.loadAllCountries(mappaStati);
		List<Border> countryPairs = dao.getCountryPairs(anno, mappaStati);
		// Inserisco i vertici
		
		
		//Graphs.addAllVertices(grafo, mappaStati.values());

		// Inserisco gli archi
		
		for (Border b : countryPairs) {
			if (!grafo.vertexSet().contains(b.getC1()))
			{
				grafo.addVertex(b.getC1());
			}
			if (!grafo.vertexSet().contains(b.getC2()))
			{
				grafo.addVertex(b.getC2());
			}
			grafo.addEdge(b.getC1(), b.getC2());
		}

		System.out.println(grafo);
	}

	public int VertexNumber() {
		return grafo.vertexSet().size();
	}

	public int EdgeNumber() {
		return grafo.edgeSet().size();
	}

	public String ElencoStatiConfini() {
		String s = "";
		for (Country c : grafo.vertexSet()) {
			if (grafo.degreeOf(c)!=0) {
			s += c.toString() + " " + grafo.degreeOf(c) + "\n";
			}
		}
		return s;
	}
	
	public int componentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> ci= new ConnectivityInspector<Country, DefaultEdge>(grafo);
		System.out.println(ci.connectedSets());
		return ci.connectedSets().size();
	}
	
	public Set<Country> getVerticiAttivi(){
		return grafo.vertexSet();
	}
	
	public Set<Country> getVicini(Country stato){
		ConnectivityInspector<Country, DefaultEdge> ci= new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return ci.connectedSetOf(stato);
	}
	
	public List<Country> getVicini1(Country stato){
		GraphIterator<Country, DefaultEdge> bfi= new BreadthFirstIterator<Country, DefaultEdge>(grafo, stato);
		List<Country> vicini= new LinkedList<Country>();
		while(bfi.hasNext())
		{
			vicini.add(bfi.next());
		}
		System.out.println(vicini.toString());
		return vicini;
	}
	
	public List<Country> getVicini2(Country stato){
		GraphIterator<Country, DefaultEdge> dfi= new DepthFirstIterator<Country, DefaultEdge>(grafo, stato);
		List<Country> vicini= new LinkedList<Country>();
		while(dfi.hasNext())
		{
			vicini.add(dfi.next());
		}
		System.out.println(vicini.toString());
		return vicini;
	}
}
