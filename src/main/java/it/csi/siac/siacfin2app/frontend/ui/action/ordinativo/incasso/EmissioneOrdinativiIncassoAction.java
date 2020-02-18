/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.incasso;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.GenericEmissioneOrdinativiAction;
import it.csi.siac.siacfin2app.frontend.ui.model.ordinativo.EmissioneOrdinativiIncassoModel;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

/**
 * Classe di action per l'emissione di ordinativi di incasso.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_INCASSO)
public class EmissioneOrdinativiIncassoAction extends GenericEmissioneOrdinativiAction<EmissioneOrdinativiIncassoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4342621546892180666L;
	
	private static final String CODICE_CLASS_STIPENDI = "RT-STI";
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	@AnchorAnnotation(value = GenericEmissioneOrdinativiAction.ANCHOR_VALUE_INCASSO, name = "Emissione Ordinativi Incasso STEP1")
	public String execute() throws Exception {
		// Fornisco solo le annotazioni in piu'
		return super.execute();
	}
	
	@Override
	protected void caricamentoDistinta() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaDistinta = sessionHandler.getParametro(BilSessionParameter.LISTA_DISTINTA);
		if(listaDistinta == null) {
			//in sessione non avevo una lista di distinte, devo ottenerla da servizio
			Liste request = model.creaRequestListe(TipiLista.DISTINTA_ENTRATA);
			logServiceRequest(request);
			ListeResponse response = genericService.liste(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaDistinta = new ArrayList<CodificaFin>(response.getDistintaEntrata());
			ComparatorUtils.sortByCodiceFin(listaDistinta);
			sessionHandler.setParametro(BilSessionParameter.LISTA_DISTINTA, listaDistinta);
		}
		model.setListaDistinta(listaDistinta);
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Svuoto i campi
		// Atto amministrativo
		model.setAttoAmministrativo(null);
		model.setTipoAtto(null);
		model.setStrutturaAmministrativoContabile(null);
		
		// Elenco
		model.setElencoDocumentiAllegato(null);
		model.setNumeroElencoDa(null);
		model.setNumeroElencoA(null);
		
		// Capitolo
		model.setCapitolo(null);
		
		// Soggetto
		model.setSoggetto(null);
	}
	
	/**
	 * Completamento dello step 1.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		// Da implementare concretamente nelle sottoclassi
		return SUCCESS;
	}
	
	@Override
	 public void prepareBackToStep1() {
		model.setListElenchi(new ArrayList<ElencoDocumentiAllegato>());
		model.setListSubdocumenti(new ArrayList<SubdocumentoEntrata>());
//		model.setCapitolo(null);
//		model.setOrdinativo(null);
		super.prepareBackToStep1();
		 
	 }
	
	//SIAC-6206
	@Override
	protected void impostaClassificatoreStipendiFiltrati(List<ClassificatoreStipendi> listaClassificatori) {
		List<ClassificatoreStipendi> listaFiltrata = new ArrayList<ClassificatoreStipendi>();
		for (ClassificatoreStipendi cs : listaClassificatori) {
			if(CODICE_CLASS_STIPENDI.equals(cs.getCodice())) {
				listaFiltrata.add(cs);
			}
		}
		model.setListaClassificatoreStipendi(listaFiltrata);
	}
}
