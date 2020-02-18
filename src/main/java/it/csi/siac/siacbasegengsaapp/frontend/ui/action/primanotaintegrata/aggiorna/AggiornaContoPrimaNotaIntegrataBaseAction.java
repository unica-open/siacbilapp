/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;
/**
 * Classe di action per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AggiornaContoPrimaNotaIntegrataBaseAction<M extends AggiornaPrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = -3158436652839795100L;
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	/**
	 * Ottiene la lista dei conti
	 * @return il risultato dell'invocazione
	 */
	public String ottieniListaConti(){
		return SUCCESS;
	}
	
	/**
	 * Ricalcolo dei totali
	 */
	protected void ricalcolaTotali (){
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		for (ElementoScritturaPrimaNotaIntegrata datoScrittura : model.getListaElementoScritturaPerElaborazione()) {
			if(datoScrittura.getMovimentoDettaglio().getImporto() != null) {
				if (OperazioneSegnoConto.AVERE.equals(datoScrittura.getMovimentoDettaglio().getSegno())){
					totaleAvere = totaleAvere.add(datoScrittura.getMovimentoDettaglio().getImporto());
				} else {
					totaleDare = totaleDare.add(datoScrittura.getMovimentoDettaglio().getImporto());
				}
			}
		}
		
		model.setTotaleAvere(totaleAvere);
		model.setTotaleDare(totaleDare);
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaConto() {
		model.setImporto(null);
		model.setOperazioneSegnoConto(null);
		model.setIndiceConto(null);
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaConto()}.
	 */
	public void validateAggiornaConto() {
		checkNotNull(model.getImporto(), "Importo");
		//SIAC-4708: per ora demando il controllo al servizio in attesa di indicazioni per risolvere la discrepanza tra completa (che per modifiche di importo impone il segno positivo) e l'inserimento automatico della PNI da modifica
		//checkCondition(model.getImporto() == null || model.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere positivo"));
		checkCondition(model.getOperazioneSegnoConto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Segno"));
		checkNotNull(model.getIndiceConto(), "Indice");
		
		int idx = model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoImportoNonModificabile(), ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore("l'utilizzo dell'importo e' non modificabile"));
	}
	
	/**
	 * Aggiorna la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaConto() {
		int idx = model.getIndiceConto().intValue();
		
		//TODO eventuialmente spostare in factory
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		elementoScrittura.getMovimentoDettaglio().setImporto(model.getImporto());
		elementoScrittura.getMovimentoDettaglio().setSegno(model.getOperazioneSegnoConto());
		elementoScrittura.getContoTipoOperazione().setOperazioneSegnoConto(model.getOperazioneSegnoConto());
		elementoScrittura.setAggiornamentoImportoManuale(true);

		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		ricalcolaTotali();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaConto()}.
	 */
	public void prepareEliminaConto() {
		model.setIndiceConto(null);
	}
	
	/**
	 * validate per Elimina la singolaRiga
	 */
	public void validateEliminaConto() {
		checkNotNull(model.getIndiceConto(), "Indice");
		
		int idx = model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoContoObbligatorio(), ErroreGEN.MOVIMENTO_CONTABILE_NON_ELIMINABILE.getErrore("l'utilizzo del conto e' obbligatorio"));
	
	}
	/**
	 * Elimina la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaConto() {
		int idx = model.getIndiceConto().intValue();
		model.getListaElementoScritturaPerElaborazione().remove(idx);
		
		ricalcolaTotali();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciConto()}.
	 */
	public void prepareInserisciConto() {
		model.setConto(null);
		model.setImportoCollapse(null);
		model.setOperazioneSegnoContoCollapse(null);
	}
	
	/**
	 * Ricerca sintetica del conto.
	 * 
	 * @param conto il conto da cercare
	 * 
	 * @return la response del servizio
	 */
	private RicercaSinteticaContoResponse ricercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto(conto);
		logServiceRequest(request);
	
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(request, response));
		}
		
		return response;
	}
	/**
	 * Validazione per il metodo {@link #inserisciConto()}.
	 */
	public void validateInserisciConto() {
		checkNotNull(model.getImportoCollapse(), "Importo");
		//SIAC-4708: per ora demando il controllo al servizio in attesa di indicazioni per risolvere la discrepanza tra completa (che per modifiche di importo impone il segno positivo) e l'inserimento automatico della PNI da modifica
		//checkCondition(model.getImporto() == null || model.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere positivo"));
		checkNotNull(model.getOperazioneSegnoContoCollapse(), "Segno");
		checkCondition(model.getConto() != null && StringUtils.isNotBlank(model.getConto().getCodice()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"), true);
		
		RicercaSinteticaContoResponse resp = ricercaSinteticaConto(model.getConto());
		
		checkCondition(!resp.getConti().isEmpty(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto", model.getConto().getCodice()), true);
		checkCondition(resp.getConti().size() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
		
		Conto conto = resp.getConti().get(0);
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un Conto foglia"), true);
		
		// Imposto il conto nel model
		model.setConto(conto);
	}
	
	/**
	 * Aggiunge un conto manualmente inserito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciConto() {
		
		MovimentoDettaglio md = new MovimentoDettaglio();
		
		md.setConto(model.getConto());
		md.setImporto(model.getImportoCollapse());
		md.setSegno(model.getOperazioneSegnoContoCollapse());
		
		ContoTipoOperazione ctop = new ContoTipoOperazione();
		ctop.setOperazioneSegnoConto(md.getSegno());
		ElementoScritturaPrimaNotaIntegrata elementoScritturaPrimaNotaIntegrata = ElementoScritturaPrimaNotaIntegrataFactory.creaElementoScritturaManuale(ctop,md);
		
		model.getListaElementoScritturaPerElaborazione().add(elementoScritturaPrimaNotaIntegrata);
		
		// Ricalcolo dei totali
		ricalcolaTotali();
		
		return SUCCESS;
	}
	
//	protected void checkScrittureCorrette() {
//		int numeroScrittureDare = 0;
//		int numeroScrittureAvere = 0;
//		
//		BigDecimal totaleDare = BigDecimal.ZERO;
//		BigDecimal totaleAvere = BigDecimal.ZERO;
//		
//		List<ElementoScritturaPrimaNotaIntegrata> elaborata = model.getListaElementoScritturaPerElaborazione();
//		
//		for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : elaborata){
//			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
//				BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
//				
//				checkCondition(elementoScrittura.getMovimentoDettaglio().getConto() != null && elementoScrittura.getMovimentoDettaglio().getConto().getUid() != 0,
//						ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("e' necessario assegnare i conti a tutte le scritture"), true);
//				if (importo == null){
//					addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
//							+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
//					continue;
//				}
//				
//				if(elementoScrittura.isSegnoDare()) {
//					numeroScrittureDare++;
//					totaleDare = totaleDare.add(importo);
//				}
//				
//				if(elementoScrittura.isSegnoAvere()) {
//					numeroScrittureAvere++;
//					totaleAvere = totaleAvere.add(importo);
//				}
//					
//			}
//		}
//		
//		checkCondition(numeroScrittureDare > 0 && numeroScrittureAvere > 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
//		checkCondition(totaleDare.subtract(totaleAvere).signum() == 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
//		
//	}

//	

//	
//	/**
//	 * Aggiornamento del conto dalla classe di conciliazione
//	 * @return una stringa corrispondente al risultato dell'invocazione
//	 */
//	public String aggiornaContoDaClasseDiConciliazione() {
//		int idx = model.getIndiceConto().intValue();
//		impostaDatiConto();
//		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
//		elementoScrittura.getContoTipoOperazione().setConto(model.getContoDaSostituire());
//		elementoScrittura.getMovimentoDettaglio().setConto(model.getContoDaSostituire());
//		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
//		ricalcolaTotali();
//		impostaInformazioneSuccesso();
//		model.setIndiceConto(null);
//		return SUCCESS;
//	}
//	
//	
//	
//	private void impostaDatiConto() {
//		if(model.getContoDaSostituire() != null && model.getContoDaSostituire().getUid() != 0){
//			log.debug("impostaDatiConto", "ho il conto!");
//			RicercaDettaglioConto req = model.creaRequestRicercaDettaglioConto(model.getContoDaSostituire());
//			RicercaDettaglioContoResponse res = contoService.ricercaDettaglioConto(req);
//			model.setContoDaSostituire(res.getConto());
//		}else{
//			log.debug("impostaDatiConto", "non ho il conto!");
//			model.setContoDaSostituire(null);
//		}
//	}
//
//	

	
}
