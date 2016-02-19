package br.leg.rr.tce.cgesi.relatorio.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.leg.rr.tce.cgesi.relatorio.entity.Auditoria;
import br.leg.rr.tce.cgesi.relatorio.entity.Portaria;
import br.leg.rr.tce.cgesi.relatorio.entity.StatusPortaria;
import br.leg.rr.tce.cgesi.relatorio.entity.UnidadeGestoraAuditoria;
import br.leg.rr.tce.cgesi.relatorio.entity.UnidadeGestoraPortaria;


@Stateless
public class StatusPortariaEjb extends AbstractEjb implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public StatusPortaria pegarStatusPortariaId(Integer id) throws Exception{
		try {
			StatusPortaria statusPortaria = new StatusPortaria();
			statusPortaria=entityManager.find(StatusPortaria.class, id);
			return statusPortaria;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	/*
	//
	public List<Portaria> findAll() throws Exception {
		try {
			String sql = "select * from scsisaudit.portaria order by id";
			List<Portaria> listaPortaria = executaSqlNativo(sql, Portaria.class, entityManager);
			return listaPortaria;

		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new Exception(" Erro" + re.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(" Erro" + e.getMessage());
		}

	}
	*/
	
}
