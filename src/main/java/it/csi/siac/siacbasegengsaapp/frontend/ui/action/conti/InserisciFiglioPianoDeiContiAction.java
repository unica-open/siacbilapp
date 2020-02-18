/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.InserisciFiglioPianoDeiContiModel;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoConto;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciFiglioPianoDeiContiAction<M extends InserisciFiglioPianoDeiContiModel> extends BaseInserisciAggiornaPianoDeiContiAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5716922411860569395L;
	
	/**
	 * popola 
	 */
	protected void caricaDatiAggiuntiviSeNecessario() {
		String methodName = "caricaDatiAggiuntiviSeNecessario";
		log.debug(methodName , "padre null? " + model.getContoPadre() == null);
		log.debug(methodName , "classe piano null? " + model.getContoPadre() == null);
		if(model.getContoPadre().getLivello() != null
				&& model.getContoPadre().getPianoDeiConti().getClassePiano().getLivelloDiLegge() != null
				&& model.getContoPadre().getLivello().equals(model.getContoPadre().getPianoDeiConti().getClassePiano().getLivelloDiLegge())){
			
			model.getConto().setContoFoglia(Boolean.TRUE);
			model.getConto().setContoDiLegge(Boolean.FALSE);
			
			model.getConto().setDescrizione(model.getContoPadre().getDescrizione());
			model.getConto().setCodiceInterno(model.getContoPadre().getCodiceInterno());
			model.getConto().setTipoConto(model.getContoPadre().getTipoConto());
			
			model.getConto().setAttivo(model.getContoPadre().getAttivo());
			model.getConto().setCodiceBilancio(model.getContoPadre().getCodiceBilancio());
			model.getConto().setContoAPartite(model.getContoPadre().getContoAPartite());
			model.getConto().setCategoriaCespiti(model.getContoPadre().getCategoriaCespiti());
			model.getConto().setElementoPianoDeiConti(model.getContoPadre().getElementoPianoDeiConti());
			model.getConto().setTipoLegame(model.getContoPadre().getTipoLegame());
			model.getConto().setContoCollegato(model.getContoPadre().getContoCollegato());
			
			model.setFiglioNonDiLegge(true);
			model.setCodiceBilancio(model.getContoPadre().getCodiceBilancio());
		}
	}
	

	/**
	 * Caricamento del conto padre e delle codifiche.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaPadreEListe() throws WebServiceInvocationFailureException {
		caricaContoPadre();
		caricaCodifiche();
	}
	
	/**
	 * Impostazione del tipo conto generico
	 */
	protected void impostaTipoContoGenerico() {
		if(model.getListaTipoConto() == null || model.getListaTipoConto().isEmpty()){
			return;
		}
		if(model.getConto() == null){
			model.setConto(new Conto());
		}
		for(TipoConto tipoConto : model.getListaTipoConto()){
			if(BilConstants.CODICE_CONTO_GENERICO.getConstant().equals(tipoConto.getCodice())){
				model.getConto().setTipoConto(tipoConto);
				return;
			}
		}
	}
	
	/**
	 * Inserimento di un conto con i dati passati in input
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String inserimento();

	/**
	 * Validazione per il metodo {@link #inserimento()}
	 */
	protected void validazioneInserimento(){
		Conto conto = model.getConto();
		checkNotNull(conto, "conto");
		checkCondition(StringUtils.isNotBlank(model.getCodiceContoEditato()), 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice conto"));
		checkCondition(StringUtils.isNotBlank(conto.getDescrizione()), 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione conto"));
		checkCondition(conto.getTipoConto() != null && conto.getTipoConto().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo conto"));
		
		ricercaEValidazioneSoggetto();
		ricercaEValidazioneContoCollegato();
		ricercaEValidazionePDC();
	}
	
	/**
	 * Caricamento del conto padre.
	 */
	private void caricaContoPadre(){
		final String methodName = "caricaContoPadre";
		Conto contoPadre = sessionHandler.getParametro(BilSessionParameter.CONTO_PADRE);
		Boolean contiFiglioSenzaFigli = sessionHandler.getParametro(BilSessionParameter.CONTI_FIGLI_SENZA_FIGLI);
		if(contoPadre != null){
			model.setContoPadre(contoPadre);
			model.setContiFiglioSenzaFigli(contiFiglioSenzaFigli);
			return;
		}
		RicercaDettaglioConto reqRDC = model.creaRequestRicercaDettaglioConto();
		logServiceRequest(reqRDC);
		RicercaDettaglioContoResponse resRDC = contoService.ricercaDettaglioConto(reqRDC);
		logServiceResponse(resRDC);
		
		if(resRDC.hasErrori()){
			log.info(methodName, createErrorInServiceInvocationString(reqRDC, resRDC));
			addErrori(resRDC);
			return;
		}
		model.setContoPadre(resRDC.getConto());
		model.setContoFinGuidataDisabled(isContoFinGuidataDisabled(resRDC.getConto().getPianoDeiConti().getClassePiano()));
		model.setContiFiglioSenzaFigli(resRDC.getContiFiglioSenzaFigli());
		sessionHandler.setParametro(BilSessionParameter.CONTO_PADRE, model.getContoPadre());
		sessionHandler.setParametro(BilSessionParameter.CONTI_FIGLI_SENZA_FIGLI, model.getContoPadre());
		sessionHandler.setParametro(BilSessionParameter.UID_CLASSE, model.getContoPadre().getPianoDeiConti().getClassePiano().getUid());
		
	}
	

}
