/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFinFactory;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinFINModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse;

/**
 * Action per i risultati di ricerca della registrazione
 * 
 * @author Valentina
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/05/2015
 * @version 1.1.0 - 05/10/2015 - adattato per GEN/GSA
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMovFinFINAction extends RisultatiRicercaRegistrazioneMovFinBaseAction<RisultatiRicercaRegistrazioneMovFinFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 415343708513109578L;
	@Autowired
	private transient ClassificatoreBilService classificatoreBilService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe();

	}
	
	/**
	 * carica le liste di titoloEntrata e titoloSpesa
	 * */
	private void caricaListe() {
		final String methodName = "caricaListaEntrata";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata == null || listaTitoloSpesa == null) {
			LeggiClassificatoriByTipoElementoBilResponse responseEntrata;
			LeggiClassificatoriByTipoElementoBilResponse responseSpesa;
			try {
				responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
				responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			} catch(WebServiceInvocationFailureException wsife) {
				log.info(methodName, wsife.getMessage());
				return;
			}
			
			listaTitoloEntrata = responseEntrata.getClassificatoriTitoloEntrata();
			listaTitoloSpesa = responseSpesa.getClassificatoriTitoloSpesa();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		model.setListaTitoloEntrata(listaTitoloEntrata);
		model.setListaTitoloSpesa(listaTitoloSpesa);
	}


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
	
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiClassificatoriByTipoElementoBil.class, response));
		}
		return response;
	}
	
	/**
	 * Legge i classificatori di spesa a partire da un conto finanziario.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String leggiClassificatoriSpesaByContoFin(){
		final String methodName = "leggiClassificatoriSpesaByContoFin";
		
		Macroaggregato macroaggregato;
		try {
			macroaggregato = ottieniMacroagregatoFromFigliRicorsiva(model.getUidPianoDeiContiRegMovFin());
		} catch(GenericFrontEndMessagesException gfeme) {
			log.debug(methodName, "Impossibile ottenere il macroaggregato ricorsivamente dai figli");
			return SUCCESS;
		}
		
		//se ha classificatori Macroaggregato
		if(macroaggregato == null){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Impossibile ottenere macroaggregato per il piano dei conti con uid: " + model.getUidPianoDeiContiRegMovFin());
			return SUCCESS;
		}
		
		model.setUidMacroaggregatoConto(macroaggregato.getUid());
		
		
		LeggiClassificatoriBilByIdFiglio reqLCBIF = model.creaRequestLeggiClassificatoriBilByIdFiglio(macroaggregato.getUid());
		LeggiClassificatoriBilByIdFiglioResponse resLCBIF = classificatoreBilService.leggiClassificatoriByIdFiglio(reqLCBIF);
		if(resLCBIF.hasErrori()){
			addErrori(resLCBIF);
			log.debug(methodName, "Impossibile ottenere titolo spesa");
			return SUCCESS;
		}
		if(resLCBIF.getClassificatoriTitoloSpesa().isEmpty()){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Impossibile ottenere titolo spesa");
			return SUCCESS;	
		}
		
		TitoloSpesa titoloSpesa = resLCBIF.getClassificatoriTitoloSpesa().get(0);
		model.setUidTitoloSpesaConto(titoloSpesa.getUid());
		//il popolamento della lista del TitoloSpesa fa gia nel prepare 
		//1) cerco il padre di titoloSpesa.getUid() con ottieniResponseLeggiClassificatoriBilByIdFiglio
		//2) cerco i figli a partire dalpadre appena trovato al punto 1 tramite ottieniResponseLeggiClassificatoriBilByIdPadre.
		//3) popolo la lista dei titoliSpesa trovata al punto 2 nel model.
	
		log.debug(methodName, "Carico la lista dei Macroaggregati spesa da servizio");
		LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getUidTitoloSpesaConto());
		sessionHandler.setParametro(BilSessionParameter.LISTA_MACROAGGREGATO, response.getClassificatoriMacroaggregato());
		model.setListaMacroaggregato(response.getClassificatoriMacroaggregato());
		
		log.debug(methodName, "Carico la lista degli ElementiPianoDeiConti da servizio");
		LeggiTreePianoDeiContiResponse responsePDC = ottieniResponseLeggiTreePianoDeiConti(model.getUidMacroaggregatoConto());
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, responsePDC.getTreeElementoPianoDeiConti());
		model.setListaElementoPianoDeiConti(responsePDC.getTreeElementoPianoDeiConti());
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene il macroaggregato ricorsivamente a partire dall'uid del piano dei conti
	 * @param uidElementoPianoDeiConti l'uid del piano dei conti
	 * @return il macroaggregato padre
	 */
	private Macroaggregato ottieniMacroagregatoFromFigliRicorsiva(int uidElementoPianoDeiConti) {
		String methodName = "ottieniMacroagregatoFromFigliRicorsiva";
		
		LeggiClassificatoriByRelazione reqLCBR = model.creaRequestLeggiClassificatoriByRelazioneDaPdC(uidElementoPianoDeiConti);
		LeggiClassificatoriByRelazioneResponse resLCBR = classificatoreBilService.leggiClassificatoriByRelazione(reqLCBR);
		if(resLCBR.hasErrori()) {
			addErrori(resLCBR);
			String msg = "Impossibile ottenere macroaggregato per il piano dei conti con uid: " + uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		if(!resLCBR.getClassificatoriMacroaggregato().isEmpty()){
			//Ho trovato il macroaggregato
			Macroaggregato macroaggregato = filtraListaByAnno(resLCBR.getClassificatoriMacroaggregato());
			if(macroaggregato == null) {
				return null;
			}
			log.info(methodName, "Trovato macroaggregato con uid: " + macroaggregato.getUid() + " collegato a ElementoPianoDeiConti con uid: " + uidElementoPianoDeiConti);
			return macroaggregato;
		}
		
		//Non ho trovato il macroaggregato.
		//Salgo di un livello del piano dei conti.
		LeggiClassificatoriBilByIdFiglio reqLCBIFFOR = model.creaRequestLeggiClassificatoriBilByIdFiglio(uidElementoPianoDeiConti);
		LeggiClassificatoriBilByIdFiglioResponse resLCBIF = classificatoreBilService.leggiClassificatoriByIdFiglio(reqLCBIFFOR);
		
		if(resLCBIF.hasErrori()) {
			addErrori(resLCBIF);
			String msg = "Impossibile ottenere elementoPianoDeiConti from figlio con uid " + uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		if(resLCBIF.getClassificatoriElementoPianoDeiConti().isEmpty()){
			addErrori(resLCBIF);
			String msg = "Nessun elementoPianoDeiConti from figlio ottenuto a partire da uid " + uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		//richiamo ricorsivo
		return ottieniMacroagregatoFromFigliRicorsiva(resLCBIF.getClassificatoriElementoPianoDeiConti().get(0).getUid());
	}

	/**
	 * Filtra la lista fornita per trovare il classificatore dell'anno
	 * @param lista la lista da filtrare
	 * @return il classificatore dell'anno
	 */
	private <T extends ClassificatoreGerarchico> T filtraListaByAnno(List<T> lista) {
		String methodName = "filtraListaByAnno";
		
		for(T t : lista){
			Date ultimoGiornoDellAnnoDiBilancio = new GregorianCalendar(model.getBilancio().getAnno(), 11, 31, 0, 0, 0).getTime();
			Date primoGiornoDellAnnoDiBilancio = new GregorianCalendar(model.getBilancio().getAnno(), 0, 1, 0, 0, 0).getTime();

					
			if(t.getDataCancellazione() == null && (t.getDataFineValidita() == null 
					|| (t.getDataFineValidita().compareTo(ultimoGiornoDellAnnoDiBilancio) <= 0)
						&& t.getDataFineValidita().compareTo(primoGiornoDellAnnoDiBilancio)>= 0)){
				//e' valido
				return t;
			}
		}
		log.info(methodName, "Nessun risultato trovato per anno corrente");
		return null;
	}
	
	/**
	 * Ottiene la categoria tipologia titolo ricorsivamente a partire dall'uid del piano dei conti
	 * @param uidElementoPianoDeiConti l'uid del piano dei conti
	 * @return la categoria tipologia titolo padre
	 */
	private CategoriaTipologiaTitolo ottieniCategoriaTipologiaTitoloFromFigliRicorsiva(int uidElementoPianoDeiConti) {
		String methodName = "ottieniCategoriaTipologiaTitoloFromFigliRicorsiva";
		
		LeggiClassificatoriByRelazione reqLCBR = model.creaRequestLeggiClassificatoriByRelazioneDaPdC(uidElementoPianoDeiConti);
		LeggiClassificatoriByRelazioneResponse resLCBR = classificatoreBilService.leggiClassificatoriByRelazione(reqLCBR);
		if(resLCBR.hasErrori()) {
			addErrori(resLCBR);
			String msg = "Impossibile ottenere CategoriaTitpologiaTitolo per il piano dei conti con uid: " + uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		if(!resLCBR.getClassificatoriCategoriaTipologiaTitolo().isEmpty()){
			// trovato
			CategoriaTipologiaTitolo categoriaTitpologiaTitolo = filtraListaByAnno(resLCBR.getClassificatoriCategoriaTipologiaTitolo());
			log.info(methodName, "Trovato tipologiaTitolo con uid: " + categoriaTitpologiaTitolo.getUid() + " collegato a ElementoPianoDeiConti con uid: " + uidElementoPianoDeiConti);
			return categoriaTitpologiaTitolo;
		}
		
		//Non ho trovato il tipologiaTitolo.
		//Salgo di un livello del piano dei conti.
		LeggiClassificatoriBilByIdFiglio reqLCBIFFOR = model.creaRequestLeggiClassificatoriBilByIdFiglio(uidElementoPianoDeiConti);
		LeggiClassificatoriBilByIdFiglioResponse resLCBIF = classificatoreBilService.leggiClassificatoriByIdFiglio(reqLCBIFFOR);
		
		if(resLCBIF.hasErrori()) {
			addErrori(resLCBIF);
			String msg = "Impossibile ottenere elementoPianoDeiConti from figlio con uid "+uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		if(resLCBIF.getClassificatoriElementoPianoDeiConti().isEmpty()){
			addErrori(resLCBIF);
			String msg = "Nessun elementoPianoDeiConti from figlio ottenuto a partire da uid "+uidElementoPianoDeiConti;
			log.error(methodName, msg);
			throw new GenericFrontEndMessagesException(msg);
		}
		
		//richiamo ricorsivo
		return ottieniCategoriaTipologiaTitoloFromFigliRicorsiva(resLCBIF.getClassificatoriElementoPianoDeiConti().get(0).getUid());
	}

	/**
	 * Legge i classificatori di entrata a partire dal conto FIN
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String leggiClassificatoriEntrataByContoFin(){
		final String methodName = "leggiClassificatoriEntrataByContoFin";
		CategoriaTipologiaTitolo categoriaTipologiaTitolo = ottieniCategoriaTipologiaTitoloFromFigliRicorsiva(model.getUidPianoDeiContiRegMovFin());
     	
		/* ********* CATEGORIA TIPOLOGIA **************** */
		//se ha classificatori tipologiaTitolo
		if(categoriaTipologiaTitolo == null){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Impossibile ottenere categoriaTipologiaTitolo per il piano dei conti con uid: " + model.getUidPianoDeiContiRegMovFin());
			return SUCCESS;	
		}
		
		model.setUidCategoriaConto(categoriaTipologiaTitolo.getUid());
		
		/* ********* TIPOLOGIA TITOLO **************** */
		LeggiClassificatoriBilByIdFiglio reqLCBR = model.creaRequestLeggiClassificatoriBilByIdFiglio(model.getUidCategoriaConto());
		LeggiClassificatoriBilByIdFiglioResponse resLCBR = classificatoreBilService.leggiClassificatoriByIdFiglio(reqLCBR);
		if(resLCBR.hasErrori()){
			addErrori(resLCBR);
			log.debug(methodName, "Impossibile ottenere TIPOLOGIA TITOLO");
			return SUCCESS;
		}
		
		if(resLCBR.getClassificatoriTipologiaTitolo().isEmpty()){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Nessun TIPOLOGIA TITOLO");
			return SUCCESS;
		}
		
		TipologiaTitolo tipologia = resLCBR.getClassificatoriTipologiaTitolo().get(0);
		model.setUidTipologiaTitoloConto(tipologia.getUid());
		
		/* ********* TITOLO **************** */
		LeggiClassificatoriBilByIdFiglio reqLCBRT = model.creaRequestLeggiClassificatoriBilByIdFiglio(model.getUidTipologiaTitoloConto());
		LeggiClassificatoriBilByIdFiglioResponse resLCBRT = classificatoreBilService.leggiClassificatoriByIdFiglio(reqLCBRT);
		if(resLCBRT.hasErrori()){
			addErrori(resLCBRT);
			log.debug(methodName, "Impossibile ottenere TITOLO");
			return SUCCESS;
		}
		
		if(resLCBRT.getClassificatoriTitoloEntrata().isEmpty()){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Nessun titolo Entrata ");
			return SUCCESS;
		}
		
		TitoloEntrata titoloEntrata = resLCBRT.getClassificatoriTitoloEntrata().get(0);
		model.setUidTitoloEntrataConto(titoloEntrata.getUid());
		
		LeggiClassificatoriBilByIdPadreResponse response = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getUidTitoloEntrataConto());
		model.setListaTitoloTipologia(response.getClassificatoriTipologiaTitolo());
		
		log.debug(methodName, "Carico la lista della categoria entrata da servizio");
		LeggiClassificatoriBilByIdPadreResponse responseCat = ottieniResponseLeggiClassificatoriBilByIdPadre(model.getUidTipologiaTitoloConto());
		model.setListaTipologiaCategoria(responseCat.getClassificatoriCategoriaTipologiaTitolo());
		
		log.debug(methodName, "Carico la lista degli ElementiPianoDeiConti da servizio");
		LeggiTreePianoDeiContiResponse responsePDC = ottieniResponseLeggiTreePianoDeiConti(model.getUidCategoriaConto());
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, responsePDC.getTreeElementoPianoDeiConti());
		model.setListaElementoPianoDeiConti(responsePDC.getTreeElementoPianoDeiConti());

		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiClassificatoriBilByIdPadreResponse}.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriBilByIdPadreResponse ottieniResponseLeggiClassificatoriBilByIdPadre(int idPadre) {
		LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(idPadre);
		return classificatoreBilService.leggiClassificatoriByIdPadre(request);
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiTreePianoDeiContiResponse}.
	 * 
	 * @param idPadre l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiTreePianoDeiContiResponse ottieniResponseLeggiTreePianoDeiConti(int idPadre) {
		LeggiTreePianoDeiConti request = model.creaRequestLeggiTreePianoDeiConti(idPadre);
		return classificatoreBilService.leggiTreePianoDeiConti(request);
	}

	
	/**
	 * Aggiornamento del piano dei conti della registrazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaPianoDeiContiRegistrazione(){
		final String methodName = "aggiornaPianoDeiContiRegistrazione";
		AggiornaElementoPianoDeiContiRegistrazioneMovFin request = model.creaRequestAggiornaElementoPianoDeiContiRegistrazioneMovFin();
		AggiornaElementoPianoDeiContiRegistrazioneMovFinResponse response = registrazioneMovFinService.aggiornaElementoPianoDeiContiRegistrazioneMovFin(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			log.debug(methodName, "Impossibile aggiornare il piano dei conti");
			return SUCCESS;
		}
		model.setRegistrazioneAggiornata(ElementoRegistrazioneMovFinFactory.getInstance(response.getRegistrazioneMovFin()));
		return SUCCESS;
	}
	
	/**
	 * Lettura del piano dei conti di spesa
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String leggiPianoDeiContiSpesa(){
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una response per il servizio di {@link LeggiClassificatoriBilByIdFiglioResponse}.
	 * 
	 * @param idFiglio l'uid del classificatore padre
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriBilByIdFiglioResponse ottieniResponseLeggiClassificatoriBilByIdFiglio(int idFiglio) {
		LeggiClassificatoriBilByIdFiglio request = model.creaRequestLeggiClassificatoriBilByIdFiglio(idFiglio);
		return classificatoreBilService.leggiClassificatoriByIdFiglio(request);
	}

}
