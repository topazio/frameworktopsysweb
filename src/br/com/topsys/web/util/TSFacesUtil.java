package br.com.topsys.web.util;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import br.com.topsys.exception.TSSystemException;
import br.com.topsys.util.TSLogUtil;
import br.com.topsys.util.TSUtil;



public final class TSFacesUtil {
    private static ResourceBundle bundle=null;

	private TSFacesUtil(){}
	
	static{
			
	    bundle =
			ResourceBundle.getBundle("config.Messages");
	    TSLogUtil.getInstance().info("config.Messages foi instanciado!");
	}
	
	public static List<SelectItem> initCombo(Collection coll,String nomeValue,String nomeLabel) {
		List<SelectItem> list=new ArrayList<SelectItem>();
		
		for(Object o:coll){
			try {
			
				list.add(new SelectItem(BeanUtils.getProperty(o,nomeValue),BeanUtils.getProperty(o,nomeLabel)));
			
			} catch (Exception e) {
				
				e.printStackTrace();
				
				throw new TSSystemException(e);
			} 
		}
		return list;
	}

   
	public static ServletContext getServletContext() {
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
	

	
	
	public static Object getManagedBean(String beanName) {
		Object o = getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());

		
		return o;
	}  
	
	
	public static void resetManagedBean(String beanName) {
		getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
	}
	
	
	
	public static void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
	}

	
	public static Object getManagedBeanInSession(String beanName) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(beanName);
	}
	
	
	public static Object removeManagedBeanInSession(String beanName) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(beanName);
	}
	
	
	public static void addObjectInSession(String beanName, Object managedBean) {
		setManagedBeanInSession(beanName,managedBean);
	}
	
	public static void addObjectInRequest(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(beanName,managedBean);
	
	}
	
	
	public static void addRequestParameter(String name, String object) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put(name, object);
	} 


	
	public static void removeObjectInSession(String beanName) {
		removeManagedBeanInSession(beanName);
	}

	public static Object getObjectInSession(String beanName) {
		return getManagedBeanInSession(beanName);
	}
	
	
	public static String getRequestParameter(String name) {
		return (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	
	}
	
	public static Object getObjectInRequest(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	
	}
	
	
	
	
	
	public static void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}
	
	public static void addInfoMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, null, msg));
	}
	
	
	private static String getText(String key){
	    String text = null;
	    
	    	try {
	      		text = bundle.getString(key);
	    	} catch (Exception e) {
	    		TSLogUtil.getInstance().warning("N�o existe essa chave no Message.properties! "+key);
	    		text = e.getMessage();
	    	}
	    return text;	
	    
	}
	
	
	public static void addMessage(String clientId, String key) {
		
    	FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(getText(key)));
  	}    
	
	
	public static HttpServletRequest getRequest(){
	    return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static HttpServletResponse getResponse(){
	    return (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
	
	
	public static FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
  
	public static void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}
	
	public static void addErrorMessageKey(String key){
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,null, getText(key)));
	}
	
	public static void addInfoMessageKey(String key){
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,null, getText(key)));
	}
	
	
	public static void addErrorMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msg));
	}
	  

	public static Integer evalInt(String el) {
		if (el == null) {
			return null;
		}
		
		if (UIComponentTag.isValueReference(el)) {
			Object value = getElValue(el);

			if (value == null) {
				return null;
			}
			else if (value instanceof Integer) {
				return (Integer)value;
			}
			else {
				return new Integer(value.toString());
			}
		}
		else {
			return new Integer(el);
		}
	}
	
	
	
	  
   
	private static Application getApplication() {
		ApplicationFactory appFactory = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication(); 
	}
	
	
	private static ValueBinding getValueBinding(String el) {
		return getApplication().createValueBinding(el);
	}
	
	
	private static HttpServletRequest getServletRequest() {
		return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	
	private static Object getElValue(String el) {
		return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
	}
	
	
	private static String getJsfEl(String value) {
		return "#{" + value + "}";
	}
	
	public static void gerarResultadoLista(List<?> lista) {

		if (TSUtil.isEmpty(lista)) {

			TSFacesUtil.addInfoMessage("A pesquisa não retornou nenhuma ocorrência");

		} else {

			Integer tamanho = lista.size();

			if (tamanho.equals(1)) {

				TSFacesUtil.addInfoMessage("A pesquisa retornou 1 ocorrência");

			} else {

				TSFacesUtil.addInfoMessage("A pesquisa retornou " + tamanho + " ocorrências");

			}

		}

	}

	public static void gerarResultadoLista(List<?> lista, String destino) {

		if (TSUtil.isEmpty(lista)) {

			TSFacesUtil.addInfoMessage(destino, "A pesquisa não retornou nenhuma ocorrência");

		} else {

			Integer tamanho = lista.size();

			if (tamanho.equals(1)) {

				TSFacesUtil.addInfoMessage(destino, "A pesquisa retornou 1 ocorrência");

			} else {

				TSFacesUtil.addInfoMessage(destino, "A pesquisa retornou " + tamanho + " ocorrências");

			}

		}

	}
	
	

	public static Cookie obterCookie(String nome) {

		Cookie cookies[] = TSFacesUtil.getRequest().getCookies();

		Cookie donaBenta = null;

		if (cookies != null) {

			for (int x = 0; x < cookies.length; x++) {

				if (cookies[x].getName().equals(nome)) {

					donaBenta = cookies[x];

					break;

				}

			}

		}

		return donaBenta;

	}

	public static void criarCookie(String nome, String valor, Integer duracao) {

		Cookie donaBenta = new Cookie(nome, valor);

		donaBenta.setMaxAge(duracao);

		TSFacesUtil.getResponse().addCookie(donaBenta);

	}
}


