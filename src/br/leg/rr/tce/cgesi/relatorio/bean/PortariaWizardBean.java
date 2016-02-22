package br.leg.rr.tce.cgesi.relatorio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import com.sun.enterprise.universal.StringUtils;

import br.leg.rr.tce.cgesi.relatorio.comum.entity.UnidadeGestora;
import br.leg.rr.tce.cgesi.relatorio.comum.util.Util;
import br.leg.rr.tce.cgesi.relatorio.ejb.AuditoriaEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.EquipeFiscalizacaoEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.PortariaEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.ServidorEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.StatusPortariaEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.UnidadeGestoraEjb;
import br.leg.rr.tce.cgesi.relatorio.ejb.UnidadeGestoraPortariaEjb;
import br.leg.rr.tce.cgesi.relatorio.entity.Auditoria;
import br.leg.rr.tce.cgesi.relatorio.entity.EquipeFiscalizacao;
import br.leg.rr.tce.cgesi.relatorio.entity.Portaria;
import br.leg.rr.tce.cgesi.relatorio.entity.PortariasAndamento;
import br.leg.rr.tce.cgesi.relatorio.entity.Servidor;
import br.leg.rr.tce.cgesi.relatorio.entity.StatusPortaria;
import br.leg.rr.tce.cgesi.relatorio.entity.UnidadeGestoraAuditoria;
import br.leg.rr.tce.cgesi.relatorio.entity.UnidadeGestoraPortaria;
import br.leg.rr.tce.cgesi.relatorio.seguranca.bean.UsuarioBean;

@Named
@SessionScoped
public class PortariaWizardBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient SistemaBean sistemaBean;

	@Inject
	private transient UsuarioBean usuarioBean;

	@Inject
	private Portaria portaria;

	@Inject
	private Auditoria auditoria;

	@Inject
	private EquipeFiscalizacao equipeFiscalizacao;

	@Inject
	private UnidadeGestora unidadeGestora;

	@Inject
	private PortariasAndamento portariasAndamento;

	@EJB
	private AuditoriaEjb auditoriaEjb;

	@EJB
	private PortariaEjb portariaEjb;

	@EJB
	private UnidadeGestoraPortariaEjb unidadeGestoraPortariaEjb;

	@EJB
	private UnidadeGestoraEjb unidadeGestoraEjb;

	@EJB
	private EquipeFiscalizacaoEjb equipeFiscalizacaoEjb;

	@EJB
	private ServidorEjb servidorEjb;

	@EJB
	private StatusPortariaEjb statusPortariaEjb;

	private List<UnidadeGestora> unidadeGestoraLista = new ArrayList<UnidadeGestora>();
	private List<Portaria> portariaList = new ArrayList<Portaria>();
	private List<UnidadeGestoraPortaria> unidadeGestoraPortariaList = new ArrayList<UnidadeGestoraPortaria>();
	private List<UnidadeGestoraPortaria> unidadeGestoraPortariaList2 = new ArrayList<UnidadeGestoraPortaria>();

	private List<UnidadeGestora> unidadeGestoraDaAuditoria = new ArrayList<UnidadeGestora>();
	private List<UnidadeGestora> unidadeGestoraSelecionadas = new ArrayList<UnidadeGestora>();
	private List<UnidadeGestora> unidadeGestoraDaPortaria = new ArrayList<UnidadeGestora>();

	private List<EquipeFiscalizacao> equipeFiscalizacaoList = new ArrayList<EquipeFiscalizacao>();

	private List<Servidor> servidorList = new ArrayList<Servidor>();

	private List<Servidor> servidorAutoridadeList = new ArrayList<Servidor>();
	private String msgTexto;

	private boolean skip;

	private boolean exibir;

	public PortariaWizardBean() {
		super();
	}

	public String editarWizardPortaria(Portaria aux) {
		try {
			portaria = portariaEjb.pegarPortaria(aux.getId());
			servidorAutoridadeList = new ArrayList<Servidor>();

			unidadeGestoraDaAuditoria = new ArrayList<UnidadeGestora>();
			unidadeGestoraSelecionadas = new ArrayList<UnidadeGestora>();
			unidadeGestoraDaPortaria = new ArrayList<UnidadeGestora>();

			// com auditoria
			if (aux.getIdAuditoria() != null) {
				auditoria = new Auditoria();
				auditoria = auditoriaEjb.carregarAuditoria(aux.getIdAuditoria());
				portaria.setAuditoria(auditoria);
				portaria.setIdAuditoria(portaria.getAuditoria().getId());

				for (UnidadeGestoraAuditoria x : portaria.getAuditoria().getUnidadeGestoraAuditorias()) {
					UnidadeGestoraPortaria unidGP = new UnidadeGestoraPortaria();
					unidGP.setUnidadeGestora(x.getUnidadeGestora());
					unidGP.setPortaria(aux);
					unidadeGestoraDaAuditoria.add(x.getUnidadeGestora());
					portaria.getListaUnidadeGestoraDaPortaria().add(unidGP);
				}

				for (UnidadeGestoraPortaria x : unidadeGestoraPortariaEjb.findIdPortaria(portaria.getId())) {
					UnidadeGestora unG = new UnidadeGestora();
					unG = x.getUnidadeGestora();
					unidadeGestoraSelecionadas.add(unG);
				}
			}else{
				for (UnidadeGestora xu : unidadeGestoraEjb.findAll()) {
					UnidadeGestoraPortaria unidGP = new UnidadeGestoraPortaria();
					unidGP.setId_unidade_gestora(xu.getId());
					unidGP.setId_portaria(aux.getId());
					unidadeGestoraDaAuditoria.add(xu);
					portaria.getListaUnidadeGestoraDaPortaria().add(unidGP);
				}
				if(!unidadeGestoraPortariaEjb.findIdPortaria(portaria.getId()).isEmpty()){
					for (UnidadeGestoraPortaria x : unidadeGestoraPortariaEjb.findIdPortaria(portaria.getId())) {
						UnidadeGestora unG = new UnidadeGestora();
						unG = x.getUnidadeGestora();
						unidadeGestoraSelecionadas.add(unG);
					}
				}
				
			}

			equipeFiscalizacaoList = new ArrayList<EquipeFiscalizacao>();
			equipeFiscalizacaoList = equipeFiscalizacaoEjb.findIdPortaria(aux.getId());

			for (Servidor stemp : servidorEjb.findAll()) {
				String vtipo = stemp.getAutoridade();
				if (vtipo.contains("S"))
					servidorAutoridadeList.add(stemp);
			}
			portaria.setNumeroPortaria(StringUtils.padLeft(portaria.getNumeroPortaria(), 3, '0'));

			return redirect("/sistema/portaria/cadastro/frmCadPortariaEtapa1.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			showFacesMessage(e.getMessage(), 4);
			return null;
		}
	}

	public void salvarMinutaPortaria() {
		try {

			// selecionandoUGP();
			/*
			 * StatusPortaria stPortaria = new StatusPortaria();
			 * stPortaria=statusPortariaEjb.pegarStatusPortariaId(1);
			 * 
			 * System.out.println(stPortaria);
			 * 
			 * PortariasAndamento pAndamento = new PortariasAndamento();
			 * 
			 * 
			 * pAndamento.setStatusDate(Util.hoje());
			 * pAndamento.setStatusJustificativa(stPortaria.getNome());
			 * pAndamento.setStatusPortaria(stPortaria.getId());
			 * pAndamento.setStatusUsr(usuarioBean.getMostraUser());
			 * portaria.setPortariasAndamentos(new
			 * ArrayList<PortariasAndamento>());
			 * portaria.addPortariasAndamento(pAndamento);
			 */
			// portariaEjb.salvar(portaria);
			portariaEjb.salvarMinuta(portaria);
			// portariaList = new ArrayList<Portaria>();
			// portariaList = portariaEjb.listaPortaria();

			// showFacesMessage("salvo com sucesso!!!", 2);
		} catch (Exception e) {
			e.printStackTrace();
			showFacesMessage(e.getMessage(), 4);
		}
	}

	// prepara editar portaria de auditoria
	public void selecionandoUGP() {
		try {
			unidadeGestoraPortariaList = new ArrayList<UnidadeGestoraPortaria>();
			unidadeGestoraPortariaList2 = new ArrayList<UnidadeGestoraPortaria>();

			Map<Integer, UnidadeGestora> mapUGS = new HashMap<Integer, UnidadeGestora>();
			Map<Integer, UnidadeGestora> mapUGE = new HashMap<Integer, UnidadeGestora>();
			Map<Integer, UnidadeGestora> mapUGP = new HashMap<Integer, UnidadeGestora>();
			// unidades selecionadas
			for (UnidadeGestora x : unidadeGestoraSelecionadas) {
				mapUGS.put(x.getId(), x);
				UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
				ugp.setPortaria(portaria);
				ugp.setUnidadeGestora(x);
				unidadeGestoraPortariaList.add(ugp);
			}
			// da auditoria
			for (UnidadeGestoraAuditoria x : portaria.getAuditoria().getUnidadeGestoraAuditorias()) {
				mapUGE.put(x.getUnidadeGestora().getId(), x.getUnidadeGestora());
				UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
				ugp.setPortaria(portaria);
				ugp.setUnidadeGestora(x.getUnidadeGestora());
				// portaria.getUnidadeGestoraPortariaExcluidas().add(ugp);
			}
			// daportaria
			for (UnidadeGestoraPortaria x : portaria.getUnidadeGestoraPortarias()) {
				mapUGP.put(x.getUnidadeGestora().getId(), x.getUnidadeGestora());
				UnidadeGestoraPortaria ugp = new UnidadeGestoraPortaria();
				ugp.setId(x.getId());
				ugp.setPortaria(portaria);
				ugp.setUnidadeGestora(x.getUnidadeGestora());
				unidadeGestoraPortariaList2.add(ugp);
				if (mapUGS.containsKey(x.getUnidadeGestora().getId())) {
					portaria.removeUnidadeGestoraPortaria(ugp);
				}
			}
			portaria.setUnidadeGestoraPortarias(getUnidadeGestoraPortariaList());
			portaria.setUnidadeGestoraPortariaExcluidas(unidadeGestoraPortariaList2);
			portaria.setEquipeFiscalizacaoList(getEquipeFiscalizacaoList());
			this.salvarMinutaPortaria();
			// this.salvar();

		} catch (Exception e) {
			e.printStackTrace();
			showFacesMessage(e.getMessage(), 4);
		}

	}

	public void exibirPainel(AjaxBehaviorEvent event) throws Exception {

		setExibir(true);

		String exEvent = "";
		exEvent = (String) event.getComponent().getAttributes().get("value");

	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			if(event.getNewStep().equals("equipe")){
				this.selecionandoUGP();
			}else{
				this.salvarMinutaPortaria();
			}
			return event.getNewStep();
		}
	}

	public void dateChange() {
		if (portaria.getPlanInicio() != null && portaria.getPlanFim() != null) {
			portaria.setPlanDiasUteis(Util.diasEntreDatas(portaria.getPlanInicio(), portaria.getPlanFim()));
		}
		if (portaria.getExecInicio() != null && portaria.getExecFim() != null) {
			portaria.setExecDiasUteis(Util.diasEntreDatas(portaria.getExecInicio(), portaria.getExecFim()));
		}
		if (portaria.getRelaInicio() != null && portaria.getRelaFim() != null) {
			portaria.setRelaDiasUteis(Util.diasEntreDatas(portaria.getRelaInicio(), portaria.getRelaFim()));
		}
	}

	public String getMsgTexto() {
		return msgTexto;
	}

	public void setMsgTexto(String msgTexto) {
		this.msgTexto = msgTexto;
	}

	public Portaria getPortaria() {
		return portaria;
	}

	public void setPortaria(Portaria portaria) {
		this.portaria = portaria;
	}

	public EquipeFiscalizacao getEquipeFiscalizacao() {
		return equipeFiscalizacao;
	}

	public void setEquipeFiscalizacao(EquipeFiscalizacao equipeFiscalizacao) {
		this.equipeFiscalizacao = equipeFiscalizacao;
	}

	public UnidadeGestora getUnidadeGestora() {
		return unidadeGestora;
	}

	public void setUnidadeGestora(UnidadeGestora unidadeGestora) {
		this.unidadeGestora = unidadeGestora;
	}

	public PortariasAndamento getPortariasAndamento() {
		return portariasAndamento;
	}

	public void setPortariasAndamento(PortariasAndamento portariasAndamento) {
		this.portariasAndamento = portariasAndamento;
	}

	public List<UnidadeGestora> getUnidadeGestoraLista() {
		return unidadeGestoraLista;
	}

	public void setUnidadeGestoraLista(List<UnidadeGestora> unidadeGestoraLista) {
		this.unidadeGestoraLista = unidadeGestoraLista;
	}

	public List<Portaria> getPortariaList() {
		return portariaList;
	}

	public void setPortariaList(List<Portaria> portariaList) {
		this.portariaList = portariaList;
	}

	public List<UnidadeGestoraPortaria> getUnidadeGestoraPortariaList() {
		return unidadeGestoraPortariaList;
	}

	public void setUnidadeGestoraPortariaList(List<UnidadeGestoraPortaria> unidadeGestoraPortariaList) {
		this.unidadeGestoraPortariaList = unidadeGestoraPortariaList;
	}

	public List<UnidadeGestoraPortaria> getUnidadeGestoraPortariaList2() {
		return unidadeGestoraPortariaList2;
	}

	public void setUnidadeGestoraPortariaList2(List<UnidadeGestoraPortaria> unidadeGestoraPortariaList2) {
		this.unidadeGestoraPortariaList2 = unidadeGestoraPortariaList2;
	}

	public List<UnidadeGestora> getUnidadeGestoraDaAuditoria() {
		return unidadeGestoraDaAuditoria;
	}

	public void setUnidadeGestoraDaAuditoria(List<UnidadeGestora> unidadeGestoraDaAuditoria) {
		this.unidadeGestoraDaAuditoria = unidadeGestoraDaAuditoria;
	}

	public List<UnidadeGestora> getUnidadeGestoraSelecionadas() {
		return unidadeGestoraSelecionadas;
	}

	public void setUnidadeGestoraSelecionadas(List<UnidadeGestora> unidadeGestoraSelecionadas) {
		this.unidadeGestoraSelecionadas = unidadeGestoraSelecionadas;
	}

	public List<UnidadeGestora> getUnidadeGestoraDaPortaria() {
		return unidadeGestoraDaPortaria;
	}

	public void setUnidadeGestoraDaPortaria(List<UnidadeGestora> unidadeGestoraDaPortaria) {
		this.unidadeGestoraDaPortaria = unidadeGestoraDaPortaria;
	}

	public List<EquipeFiscalizacao> getEquipeFiscalizacaoList() {
		return equipeFiscalizacaoList;
	}

	public void setEquipeFiscalizacaoList(List<EquipeFiscalizacao> equipeFiscalizacaoList) {
		this.equipeFiscalizacaoList = equipeFiscalizacaoList;
	}

	public List<Servidor> getServidorList() {
		return servidorList;
	}

	public void setServidorList(List<Servidor> servidorList) {
		this.servidorList = servidorList;
	}

	public List<Servidor> getServidorAutoridadeList() {
		return servidorAutoridadeList;
	}

	public void setServidorAutoridadeList(List<Servidor> servidorAutoridadeList) {
		this.servidorAutoridadeList = servidorAutoridadeList;
	}

	public boolean isExibir() {
		return exibir;
	}

	public void setExibir(boolean exibir) {
		this.exibir = exibir;
	}

}
