/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ognl.OgnlValueStack;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2app.frontend.ui.action.documento.RisultatiRicercaDocumentoEntrataAction;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CollegaDocumentoEntrataAction extends RisultatiRicercaDocumentoEntrataAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2837315976427746465L;
	
	private PreDocumentoEntrata preDoc;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		model.setTitolo("AGGIORNA PREDISPOSIZIONE D'INCASSO - COLLEGA DOCUMENTO");
		setPredocImportoDaAssociare();
	}

	/**
	 * passo al model l'importo in sessione
	 */
	private void setPredocImportoDaAssociare() {
		if(sessionHandler.containsKey(BilSessionParameter.IMPORTO_PREDOC_TO_COMPARE) 
				&& sessionHandler.getParametro(BilSessionParameter.IMPORTO_PREDOC_TO_COMPARE) != null) {
			model.setImportoPreDocumento((BigDecimal) sessionHandler.getParametro(BilSessionParameter.IMPORTO_PREDOC_TO_COMPARE));
//			sessionHandler.setParametro(BilSessionParameter.IMPORTO_PREDOC_TO_COMPARE, null);
		}
	}

	/**
	 * passo al model l'importo in sessione
	 */
	private void setuidSubdocDaAssociare() {
		model.setUidSubDocumentoDaAssociare(new Integer(getParameterToCheck("uid")));
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return super.execute();
	}
	
	public String checkWithWarning() {
		final String methodName = "checkWithWarning";
		
		setuidSubdocDaAssociare();
		
		log.debug(methodName, "Collega documento al PreDocumento di Entrata");
		
		if(checkPredocDaSpezzare() && Boolean.FALSE.equals(model.isProseguireConElaborazione())) {			
			Messaggio mess = new Messaggio();
			mess.setCodice("BIL_WAR_0115");
			mess.setDescrizione("L’importo della quota fattura &egrave; inferiore all’importo della predisposizione, la predisposizione verr&agrave;  spezzata, vuoi proseguire?");
			addMessaggio(mess);
		}

		if(compareImputazioniContabili() && Boolean.FALSE.equals(model.isProseguireConElaborazione())) {			
			Messaggio mess = new Messaggio();
			mess.setCodice("BIL_WAR_0116");
			mess.setDescrizione("Le imputazioni contabili della predisposizione sono diverse da quelle della fattura, al salvataggio verranno sostituite, vuoi proseguire?");
			addMessaggio(mess);
		}
		
		if(getActionMessages() != null && getActionMessages().size() > 0) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	private boolean checkPredocDaSpezzare() {
		boolean result = false;
						
		if(model.getImportoPreDocumento() != null && model.getImportoPreDocumento().compareTo(BigDecimal.ZERO) > 0) {
			
			String importo = getParameterToCheck("importo");
			BigDecimal imp = new BigDecimal(importo);
			
			result = model.getImportoPreDocumento().compareTo(imp) > 0;
			
		}	
		
		return result;
	}

	/**
	 * @param preDoc, codiceProvvCassaQuota
	 * @return boolean
	 */
	private boolean compareImputazioniContabili() {
		boolean result = false;
		
		takeMethodFromSessionIfExists();

		String codiceProvvCassaQuota = getParameterToCheck("provvisorioCassa");
		
		if((preDoc != null && preDoc.getProvvisorioDiCassa() != null
				&& preDoc.getProvvisorioDiCassa().getAnno() != null 
				&& preDoc.getProvvisorioDiCassa().getNumero() != null)) {
			
			Integer annoProvvQuota = new Integer(codiceProvvCassaQuota.substring(0, 3));
			Integer numeroProvvQuota = new Integer(codiceProvvCassaQuota.substring(5, codiceProvvCassaQuota.length()));
			
			if((annoProvvQuota.compareTo(preDoc.getProvvisorioDiCassa().getAnno()) != 0 
					|| numeroProvvQuota.compareTo(preDoc.getProvvisorioDiCassa().getNumero()) != 0)) {
				result = true;
			}
			
		} else if(codiceProvvCassaQuota != null) {
			result = true;
		}

		String codiceSoggetto = getParameterToCheck("soggetto");
		
		if((preDoc != null 
				&& preDoc.getSoggetto() != null 
				&& preDoc.getSoggetto().getCodiceSoggettoNumber() != null)) {
			
			String[] arrCod = codiceSoggetto.split(" - ");
			BigInteger bigInt = new BigInteger(arrCod[0]);
			
			if((bigInt.compareTo(preDoc.getSoggetto().getCodiceSoggettoNumber()) != 0)) {
				result = true;
			}
			
		} else if(codiceSoggetto != null) {
			result = true;
		}

		String codiceAccertamento = getParameterToCheck("movimento");
		
		if((preDoc != null
				&& preDoc.getAccertamento() != null)) {
			
			String[] arrCod = codiceAccertamento.split("/");
			Integer intAnno = new Integer(arrCod[0]);
			BigDecimal bigD = new BigDecimal(arrCod[arrCod.length-1]);
			
			if((bigD.compareTo(preDoc.getAccertamento().getNumeroBigDecimal()) != 0
					|| intAnno.compareTo(preDoc.getAccertamento().getAnnoMovimento()) != 0)) {
				result = true;
			}
			
		} else if(codiceAccertamento != null) {
			result = true;
		}
		
		return result;
	}

	/**
	 * return il codice anno/numero del provvisorio di cassa a cui e' associata la quota
	 */
	private String getParameterToCheck(String paramName) {
		String codice = null;
		codice = getParameterFromStrutsRequest(paramName);
		return codice;
	}
	
	/**
	 * @param preDoc
	 * @return
	 */
	private void takeMethodFromSessionIfExists() {
		if(sessionHandler.containsKey(BilSessionParameter.PREDOC_SELEZIONATO) 
				&& sessionHandler.getParametro(BilSessionParameter.PREDOC_SELEZIONATO) != null){	
			preDoc = sessionHandler.getParametro(BilSessionParameter.PREDOC_SELEZIONATO);
		}
	}

	
	private String getParameterFromStrutsRequest(String keyValueToRetrieve) {
		
		String[] oggettoTemporaneo = null;
		
		OgnlValueStack ovs = (OgnlValueStack) request.get("struts.valueStack");
		HashMap<String,Object> contextMap = (HashMap<String, Object>) ovs.getContext().get("parameters");
		Set<String> set = contextMap.keySet();
		
		for(String key : set) {
			if(key.equals(keyValueToRetrieve)) {
				oggettoTemporaneo = (String[]) contextMap.get(key);
			}
			
		}
		
		return oggettoTemporaneo[0];
	}
	
}
