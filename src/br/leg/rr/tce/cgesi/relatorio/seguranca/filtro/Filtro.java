package br.leg.rr.tce.cgesi.relatorio.seguranca.filtro;

import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.leg.rr.tce.cgesi.relatorio.seguranca.bean.UsuarioBean;
import br.leg.rr.tce.cgesi.relatorio.seguranca.ejb.UsuarioEjb;
import br.leg.rr.tce.cgesi.relatorio.seguranca.util.PropriedadesSistema;

public class Filtro implements Filter {

	public Filtro() {
	}

	@Inject
	private transient UsuarioBean usuarioBean;

	@EJB
	private UsuarioEjb usuarioEjb;

	public void init(FilterConfig filterconfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String origem = ((HttpServletRequest) request).getHeader("Referer");
		if (origem == null) {
			origem = "nulo";
		}

		//FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest requestExterno = (HttpServletRequest) request;
		//HttpServletRequest requestExterno = (HttpServletRequest) context.getExternalContext().getRequest();

		if (usuarioBean.getUsuario() == null) {
			if (requestExterno.getRemoteUser() != null)
				usuarioBean.preencherUsuarioLogado();
		}

		// Servidor usu = new Servidor();
		HttpSession s = ((HttpServletRequest) request).getSession();
		// usu = (Servidor) s.getAttribute("s_usuario");
		// if (usu != null) {
		// System.out.println("Matricula: " + usu.getMatricula());
		// }

		if (usuarioBean.getUsuario() != null) {
			chain.doFilter(request, response);

		} else {
			s.invalidate();
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(PropriedadesSistema.SERVIDOR_REDIRECT);
		}

	}

	public void destroy() {
	}
}
