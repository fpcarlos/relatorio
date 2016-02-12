package br.leg.rr.tce.cgesi.relatorio.seguranca.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.leg.rr.tce.cgesi.relatorio.entity.Servidor;

public class Filtro implements Filter {

	public Filtro() {
	}

	public void init(FilterConfig filterconfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String origem = ((HttpServletRequest) request).getHeader("Referer");
		if (origem == null) {
			origem = "nulo";
		}

		Servidor usu = new Servidor();
		HttpSession s = ((HttpServletRequest) request).getSession();
		usu = (Servidor) s.getAttribute("s_Servidor");
/*
		if (origem.equals(PropriedadesSistema.SERVIDOR_ORIGEM) || usu != null) {
			if (usu != null) {
				chain.doFilter(request, response);
			} else {
				usu = new Servidor();
				if (request.getParameter("login") != null) {
					if (request.getParameter("login") == null) {
						usu.getServidor().setLogin("nulo");
					} else {
						usu.getServidor().setLogin(
								request.getParameter("login"));
					}

					// if (request.getParameter("matricula") == null) {
					// usu.setMatricula("nulo");
					// } else {
					// usu.setMatricula(request.getParameter("matricula"));
					// }
					// MenuDao dao = new MenuDao();
					// List<Menu> menus = new ArrayList<Menu>();
					// menus = dao.buscaPorMenuServidor(usu.getLogin());
					// for (Menu menu : menus) {
					// usu.getContextos().add(menu.getContexto());
					// }

					s.setAttribute("s_Servidor", usu);

				} else {
					// System.out.println("Servidor não logado");
					s.invalidate();
					HttpServletResponse res = (HttpServletResponse) response;
					res.sendRedirect(PropriedadesSistema.SERVIDOR_REDIRECT);
				}
				try {
					chain.doFilter(request, response);
				} catch (Exception e) {

				}

			}
		} else {
			// System.out
			// .println("SETENÇA DIFERENDE DE ===>>>  if (origem.equals(PropriedadesSistema.SERVIDOR_ORIGEM) || usu != null) {");
			// System.out.println("Origem " + origem);
			// System.out.println("Servidor " + usu);
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(PropriedadesSistema.SERVIDOR_REDIRECT);
		}
		
	*/
	}

	public void destroy() {
	}
	
}
