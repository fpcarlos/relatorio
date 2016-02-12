package br.leg.rr.tce.cgesi.relatorio.seguranca.bean;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.leg.rr.tce.cgesi.relatorio.bean.AbstractBean;
import br.leg.rr.tce.cgesi.relatorio.entity.Grupo;
import br.leg.rr.tce.cgesi.relatorio.entity.Servidor;
import br.leg.rr.tce.cgesi.relatorio.seguranca.ejb.UsuarioEjb;

@Named
@SessionScoped
public class UsuarioBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	protected UsuarioEjb usuarioEjb;
	@Inject
	protected Conversation conversation;
	
	protected Servidor usuario;
	protected Servidor usuarioSelected;
	protected Grupo grupo;
	protected Grupo grupoSelected;

	//protected Status status;
	//protected Status grupoStatus;
	//protected boolean operatingGrupo;
	//protected boolean changePassword;

	
	@PostConstruct
    public void init() {
        setUsuario(new Servidor());
        //setStatus(Status.LISTING);
        //setGrupoStatus(Status.LISTING);
        //setOperatingGrupo(false);
        //setChangePassword(false);
    }

	
	public List<Servidor> getUsuarios() {
        try {
            return this.usuarioEjb.retrieveUsuarios();
        } catch (Exception exception) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lista de usuarios: ", exception.getMessage()));
            return null;
        }
    }
	
	
	public void viewUsuario() {
        try {
            if (getUsuarioSelected() != null) {
                setUsuario(getUsuarioSelected());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Visualizar: ", " usuario ("
                        + getUsuario().getId() + ")"));
                //setStatus(Status.VIEWING);
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Falha: ", "Não há usuario selecionada para visualizar."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Falha: ", "Erro ao visualizar: " + e.getMessage()));
        }
    }
	
	
	public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (Exception e) {
        }
        return "/index.xhtml";
    }

    public boolean isUserLoggedIn() {
        if(remoteUser() != null) {
            return true;
        }
        return false;
    }
	
    public String remoteUser() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            return request.getRemoteUser();
        } catch (Exception e) {
            return null;
        }

    }
    public String getMostraUser(){
    	String aux1 = remoteUser();
    	Principal aux2 = (Principal) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    	if(remoteUser() != null) {
    		
            return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();            
    	}
    	return null;
    }
    
    public Servidor getUsuarioLogado(){
    	try {
    		if(remoteUser() != null) {
                return usuarioEjb.pegaLogado();            
        	}
        	return null;
		} catch (Exception e) {
			return null;
		}
    	
    }
    
	public Servidor getUsuario() {
		return usuario;
	}

	public void setUsuario(Servidor usuario) {
		this.usuario = usuario;
	}
	
	
	public Servidor getUsuarioSelected() {
		return usuarioSelected;
	}


	public void setUsuarioSelected(Servidor usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}


	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Grupo getGrupoSelected() {
		return grupoSelected;
	}

	public void setGrupoSelected(Grupo grupoSelected) {
		this.grupoSelected = grupoSelected;
	}
	
	
	
	
	
	
	/*
	 * private final PrincipalImpl principal;
	 * 
	 * @Inject public AutenticacaoServiceBean(Principal principal,
	 * UsuarioRepository usuarioRepository) { this.principal = principal;
	 * this.usuarioRepository = usuarioRepository; }
	 */

}
