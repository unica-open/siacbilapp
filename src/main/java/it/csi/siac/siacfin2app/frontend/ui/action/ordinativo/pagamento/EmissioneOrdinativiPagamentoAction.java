/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.pagamento;

import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimentoResponse;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
//import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.GenericEmissioneOrdinativiAction;
import it.csi.siac.siacfin2app.frontend.ui.model.ordinativo.EmissioneOrdinativiPagamentoModel;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

/**
 * Classe di action per l'emissione di ordinativi di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_PAGAMENTO)
public class EmissioneOrdinativiPagamentoAction extends GenericEmissioneOrdinativiAction<EmissioneOrdinativiPagamentoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1185115768387523832L;
	
	private static final String CODICE_CLASS_STIPENDI = "STI";

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	@AnchorAnnotation(value = GenericEmissioneOrdinativiAction.ANCHOR_VALUE_PAGAMENTO, name = "Emissione Ordinativi Pagamento STEP1")
	public String execute() throws Exception {
		// Fornisco solo le annotazioni in piu'
		return super.execute();
	}
	
	@Override
	protected void caricamentoDistinta() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaDistinta = sessionHandler.getParametro(BilSessionParameter.LISTA_DISTINTA);
		if(listaDistinta == null) {
			Liste req = model.creaRequestListe(TipiLista.DISTINTA);
			logServiceRequest(req);
			ListeResponse res = genericService.liste(req);
			logServiceResponse(res);
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(Liste.class, res));
			}
			listaDistinta = new ArrayList<CodificaFin>(res.getDistinta());
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
	
	/**
	 * Preparazione per il metodo {@link #controllaDisponibilitaDiCassaCapitoli()}
	 */
	public void prepareControllaDisponibilitaDiCassaCapitoli() {
		model.setIdsElenchi(new ArrayList<Integer>());
		model.setIdsSubdocumentiSpesa(new ArrayList<Integer>());
	}
	
	/**
	 * Controlla disponibilita di cassa capitoli.
	 *
	 * @return the string
	 */
	public String controllaDisponibilitaDiCassaCapitoli() {
		cleanErroriMessaggiInformazioni();
		ControllaDisponibilitaCassaCapitoloByMovimento req = model.creaRequestControllaDisponibilitaCassaCapitoloByMovimento();
		ControllaDisponibilitaCassaCapitoloByMovimentoResponse res= emissioneOrdinativiService.controllaDisponibilitaCassaCapitoloByMovimento(req);
		if(res.hasErrori()) {
			addErrori(res);
			return SUCCESS;
		}
		addInformazione(new Informazione("CRU_CON_2001", "Lo stanziamento di cassa dei capitoli e' sufficiente per emettere gli ordinativi."));
		return SUCCESS;
	}
	
	 @Override
	 public void prepareBackToStep1() {
		model.setIdsSubdocumentiSpesa(new ArrayList<Integer>());
		model.setIdsElenchi(new ArrayList<Integer>());
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
	

	//task-259
	//task-291
	protected void caricaCommissioniDefault() {
		if(abilitaCampoDefaultCommissioniInserimentoOrdinativiPagamento()) {
			if(model.getListaCommissioniDocumento().size()>1){
				for(CommissioneDocumento lista : model.getListaCommissioniDocumento()){
					if(CommissioniDocumento.ESENTE.getDescrizione().toUpperCase().equals(lista.getDescrizione().toUpperCase())){
						model.setCommissioneDocumento(lista);
						break;
					}
				}
			}
		}
	}
		
	//task-259
	private boolean abilitaCampoDefaultCommissioniInserimentoOrdinativiPagamento() {
		return Boolean.TRUE.equals(Boolean.parseBoolean(getParametroConfigurazioneEnte(
				ParametroConfigurazioneEnteEnum.INSERISCI_ORDINATIVO_PAGAMENTO_DEFAULT_COMMISSIONI)));
	}
	
}
