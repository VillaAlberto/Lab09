package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> mappa) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country cTemp= new Country(rs.getString("StateAbb"), rs.getInt("ccode"),rs.getString("StateNme"));
				if (!mappa.containsKey(cTemp.getCode()))
						{
					mappa.put(cTemp.getCode(), cTemp);
						}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> mappa) {
		
		String sql="SELECT DISTINCT dyad, state1no, state2no FROM contiguity WHERE YEAR<=? AND conttype=1";
		List<Border> coutryPairs= new LinkedList<Border>();
		
		try {
			Connection conn= ConnectDB.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				Country c1= mappa.get(rs.getInt("state1no"));
				Country c2=mappa.get(rs.getInt("state2no"));
				int id= rs.getInt("dyad"); 
				Border border= new Border(id, c1, c2);
				
				if (!coutryPairs.contains(border))
				{
					coutryPairs.add(border);
				}
			}
			conn.close();
			return coutryPairs;
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
