/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
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
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;

/**
 * Classe base di action per l'inserimento e l'aggiornamento dell'elencoscritture della prima nota libera
 *   
 * @author Paggio Simona
 * @version 1.0.0 - 14/04/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 08/10/2015
 *
 * @param <M> la tipizzazione del model
 */
public abstract class BaseInserisciAggiornaContoPrimaNotaLiberaBaseAction <M extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel> extends BaseInserisciAggiornaPrimaNotaLiberaBaseAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3431092717404597597L;
	
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	
	/**
	 * Ricalcolo dei totali
	 */
	private void ricalcolaTotali (){
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		for (ElementoScritturaPrimaNotaLibera datoScrittura : model.getListaElementoScritturaPerElaborazione()) {
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
		model.setDaRegistrare(totaleAvere.subtract(totaleDare));
	}
	
	/**
	 * Ottiene la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaConti() {
		ricalcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciConto()}.
	 */
	public void prepareInserisciConto() {
		model.setConto(null);
		model.setImportoCollapse(null);
		model.setOperazioneSegnoContoCollapse(null);
		model.setMissione(null);
		model.setProgramma(null);
	}
	
	/**
	 * Aggiunge un conto manualmente inserito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciConto() {
		
		MovimentoDettaglio mD = new MovimentoDettaglio();
		
		mD.setConto(model.getConto());
		mD.setImporto(model.getImportoCollapse());
		mD.setSegno(model.getOperazioneSegnoContoCollapse());
		mD.setMissione(model.getMissione());
		mD.setProgramma(model.getProgramma());
		
		ContoTipoOperazione cTop = new ContoTipoOperazione();
		cTop.setOperazioneSegnoConto(mD.getSegno());
		ElementoScritturaPrimaNotaLibera elementoScritturaPNL = ElementoScritturaPrimaNotaLiberaFactory.creaElementoScritturaManuale(cTop,mD);
		
		model.getListaElementoScritturaPerElaborazione().add(elementoScritturaPNL);
		
		// Ricalcolo dei totali
		ricalcolaTotali();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciConto()}.
	 */
	public void validateInserisciConto() {
		checkNotNull(model.getImportoCollapse(), "Importo");
		// SIAC-5719: gli importi possono anche essere negativi, ma non pari a zero
		checkCondition(model.getImporto() == null || model.getImporto().signum() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": non puo' essere pari a zero"));
		checkNotNull(model.getOperazioneSegnoContoCollapse(), "Segno");
		checkCondition(model.getConto() != null && StringUtils.isNotBlank(model.getConto().getCodice()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"), true);
		
		RicercaSinteticaContoResponse resp = ricercaSinteticaConto(model.getConto());
		
		checkCondition(!resp.getConti().isEmpty(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto", model.getConto().getCodice()), true);
		checkCondition(resp.getConti().size() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
		
		Conto conto = resp.getConti().get(0);
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un Conto foglia"), true);
		
		// Imposto il conto nel model
		model.setConto(conto);
		
		// SIAC-5281
		popolaMissioneProgramma();
	}

	/**
	 * Popolamento di missione e programma da sessione
	 */
	private void popolaMissioneProgramma() {
		if(!isContoCE()) {
			// Il conto non e' CE: pulisco missione e programma
			model.setMissione(null);
			model.setProgramma(null);
			return;
		}
		
		List<Missione> listaMissione = model.getListaMissione();
		List<Programma> listaProgramma = sessionHandler.getParametro(BilSessionParameter.LISTA_PROGRAMMA);
		Missione missione = ComparatorUtils.searchByUidEventuallyNull(listaMissione, model.getMissione());
		Programma programma = ComparatorUtils.searchByUidEventuallyNull(listaProgramma, model.getProgramma());
		
		
		// SIAC-5690: Clono i dati per evitare problemi dovuti alla pulizia del campo
		model.setMissione(ReflectionUtil.deepClone(missione));
		model.setProgramma(ReflectionUtil.deepClone(programma));
	}
	
	/**
	 * Controlla se il conto sia CE
	 * @return
	 */
	private boolean isContoCE() {
		return model.getConto() != null
				&& model.getConto().getPianoDeiConti() != null
				&& model.getConto().getPianoDeiConti().getClassePiano() != null
				&& BilConstants.CLASSE_CONTO_COSTI_DI_ESERCIZIO.getConstant().equals(model.getConto().getPianoDeiConti().getClassePiano().getCodice());
	}

	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaConto() {
		model.setConto(null);
		model.setImporto(null);
		model.setOperazioneSegnoConto(null);
		model.setIndiceConto(null);
		model.setMissione(null);
		model.setProgramma(null);
	}
	
	/**
	 * Aggiorna la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaConto() {
		int idx = model.getIndiceConto().intValue();
		
		ElementoScritturaPrimaNotaLibera elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		elementoScrittura.getMovimentoDettaglio().setImporto(model.getImporto());
		if (!model.isContiCausale()) {
			elementoScrittura.getMovimentoDettaglio().setSegno(model.getOperazioneSegnoConto());
			elementoScrittura.getContoTipoOperazione().setOperazioneSegnoConto(model.getOperazioneSegnoConto());
		}
		elementoScrittura.getMovimentoDettaglio().setMissione(model.getMissione());
		elementoScrittura.getMovimentoDettaglio().setProgramma(model.getProgramma());
		elementoScrittura.setAggiornamentoImportoManuale(true);

		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		ricalcolaTotali();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaConto()}.
	 */
	public void validateAggiornaConto() {
		checkNotNull(model.getImporto(), "Importo");
		// SIAC-5719: gli importi possono anche essere negativi, ma non pari a zero
		checkCondition(model.getImporto() == null || model.getImporto().signum() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": non puo' essere pari a zero"));
		checkNotNull(model.getOperazioneSegnoConto(), "Segno");
		checkNotNull(model.getIndiceConto(), "Indice", true);
		
		int idx =  model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		// SIAC-5281
		popolaMissioneProgramma();
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
		String methodName = "validateEliminaConto";
		checkNotNull(model.getIndiceConto(), "Indice", true);
		int idx =  model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		//non posso eliminarlo se e' tra i conti provenienti dalla causale
		ElementoScritturaPrimaNotaLibera contoDaEliminare = model.getListaElementoScritturaPerElaborazione().get(idx);
		
		log.debug(methodName, "codice conto da eliminare: " + contoDaEliminare.getCodiceConto());
		for(ElementoScritturaPrimaNotaLibera es : model.getListaElementoScritturaDaCausale()){
			log.debug(methodName, "codice conto da confrontare: "+  es.getCodiceConto());
			if(es.getCodiceConto().equals(contoDaEliminare.getCodiceConto())){
				checkCondition(false , ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto deriva da una causale"), true);
			}
		}
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
	 * Preparazione per il metodo {@link #impostaImportoDaRegistrare()}
	 */
	public void prepareImpostaImportoDaRegistrare() {
		model.setImportoDaRegistrare(null);
	}
	
	/**
	 * Validate per aggiornamento importo proposto
	 * 
	 */
	public void validateImpostaImportoDaRegistrare() {
		checkCondition(model.isContiCausale(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("I conti non sono associati a una causale"), true);
		
	}
	
	/**
	 * Aggiorna GLi importi della lista dei conti se necessario in base all'importo inserito la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String impostaImportoDaRegistrare() {
		List<ElementoScritturaPrimaNotaLibera> importiDaRegistrare = aggiornaImportoDaRegistrare (model.getListaElementoScritturaPerElaborazione(), model.getImportoDaRegistrare());
		model.setListaElementoScritturaPerElaborazione(importiDaRegistrare);
		
		ricalcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Aggiorna l'importo da registrare.
	 * 
	 * @param listaScritture      la lista delle scritture
	 * @param importoDaRegistrare l'importo da registrare
	 * 
	 * @return la lista delle scritture aggiornate
	 */
	private List<ElementoScritturaPrimaNotaLibera> aggiornaImportoDaRegistrare(List<ElementoScritturaPrimaNotaLibera> listaScritture, BigDecimal importoDaRegistrare){
		List<ElementoScritturaPrimaNotaLibera> listaRisultato = new ArrayList<ElementoScritturaPrimaNotaLibera>();
		
		for (ElementoScritturaPrimaNotaLibera datoScrittura : listaScritture) {
			// Imposto l'importo daregistrare se e' da fare
			if (OperazioneUtilizzoImporto.PROPOSTO.equals(datoScrittura.getContoTipoOperazione().getOperazioneUtilizzoImporto())
					&& (datoScrittura.getMovimentoDettaglio().getImporto() == null
						|| datoScrittura.getMovimentoDettaglio().getImporto().signum() == 0
						// Caso in cui devo sovrascrivere un automatismo precedente
						|| !datoScrittura.isAggiornamentoImportoManuale()
					)) {
				datoScrittura.getMovimentoDettaglio().setImporto(importoDaRegistrare);
			}
			listaRisultato.add(datoScrittura);
		}
		return listaRisultato;
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
	 * Risultato per i conti della prima nota libera.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 12/05/2015
	 *
	 */
	public static class ContoPrimaNotaLiberaResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -8197648894246732184L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaElementoScritturaPerElaborazione.*, totaleDare.*, totaleAvere.*, daRegistrare.*, contiCausale.*,singoloContoCausale,inserisciNuoviContiAbilitato";

		/** Empty default constructor */
		public ContoPrimaNotaLiberaResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
}
