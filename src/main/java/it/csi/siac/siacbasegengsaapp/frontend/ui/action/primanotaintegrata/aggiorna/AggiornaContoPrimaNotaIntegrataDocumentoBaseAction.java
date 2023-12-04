/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.importi.ImportiDareAvere;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;
/**
 * Classe di action per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AggiornaContoPrimaNotaIntegrataDocumentoBaseAction<M extends AggiornaPrimaNotaIntegrataDocumentoBaseModel> extends GenericBilancioAction<M> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = -4762528313136702958L;
	
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	/**
	 * Prepare ottieni lista conti.
	 */
	public void prepareOttieniListaConti() {
		model.setRegistrazioneInElaborazione(null);
	}
	
	/**
	 * Ottiene la lista dei conti
	 * @return il risultato dell'invocazione
	 */
	public String ottieniListaConti(){
		if(model.getUidMovimentoEPPerScritture() == 0){
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid movimento EP"));
			return SUCCESS;
		}
		List<ElementoScritturaPrimaNotaIntegrata> lista =  model.getMappaMovimentoEPScritture().get(model.getUidMovimentoEPPerScritture());
		//SIAC-6227
		model.setRegistrazioneInElaborazione(estraiRegistrazioneDaListaInElaborazione(lista));
		// SIAC-5802: devo clonare la lista
		model.setListaElementoScritturaPerElaborazione(ReflectionUtil.deepClone(lista));
		ricalcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #salvaDatiQuota()}
	 */
	public void prepareSalvaDatiQuota () {
		model.setProseguiImportiNonCongruenti(false);
	}
	
	/**
	 * Validazione per il metodo {@link #salvaDatiQuota()}
	 */
	public void validateSalvaDatiQuota() {
		checkScrittureCorrette();
		if(!hasErrori() && !model.isProseguiImportiNonCongruenti()) {
			askImportiScritture();
		}
	}
	
	/**
	 * salva i dati nel movimento EP
	 * @return  il risultato dell'invocazione
	 */
	public String salvaDatiQuota(){
		if(hasMessaggi()) {
			return ASK;
		}
		
		model.getMappaMovimentoEPScritture().put(model.getUidMovimentoEPPerScritture(),model.getListaElementoScritturaPerElaborazione());
		model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
		model.setUidMovimentoEPPerScritture(0);
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
		checkCondition(model.getImporto() == null || model.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere positivo"));
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
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(RicercaSinteticaConto.class, response));
		}
		
		return response;
	}
	/**
	 * Validazione per il metodo {@link #inserisciConto()}.
	 */
	public void validateInserisciConto() {
		checkNotNull(model.getImportoCollapse(), "Importo");
		checkCondition(model.getImporto() == null || model.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere positivo"));
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
	
	/**
	 * Controlla che le scritture siano corrette. Devono essere:
	 * <ul>
	 *     <li>
	 *         presenti almeno 2 scritture su conti con segni differenti (pu&ograve; essere anche lo stesso conto devono essere diversi i segni),
	 *         altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Devono essere presenti almeno due conti con segni differenti.')&gt;</code>
	 *     </li>
	 *     <li>
	 *         il totale dare deve essere uguale al totale avere: altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Il totale DARE deve essere UGUALE al totale AVERE.')&gt;</code>
	 *     </li>
	 * </ul>
	 */
	protected void checkScrittureCorrette() {
		ImportiDareAvere wrapper = calcolaImportoDareAvere(model.getListaElementoScritturaPerElaborazione(), true);
		
		checkCondition(wrapper.getNumeroScrittureDare() > 0 && wrapper.getNumeroScrittureAvere() > 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
		checkCondition(wrapper.getTotaleDare().subtract(wrapper.getTotaleAvere()).signum() == 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
	}
	
	/**
	 * Calcolo degli importi in dare e avere
	 * @param scritture le scritture
	 * @param addErrori se aggiungere gli errori
	 * @return gli importi
	 */
	protected ImportiDareAvere calcolaImportoDareAvere(List<ElementoScritturaPrimaNotaIntegrata> scritture, boolean addErrori) {
		ImportiDareAvere wrapper = new ImportiDareAvere();
		
		for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : scritture){
			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
				BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
				
				checkCondition(!addErrori || (elementoScrittura.getMovimentoDettaglio().getConto() != null && elementoScrittura.getMovimentoDettaglio().getConto().getUid() != 0),
						ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("e' necessario assegnare i conti a tutte le scritture"), true);
				
				if(importo != null) {
					// Aggiungo i dati al segno dare o avere
					if(elementoScrittura.isSegnoDare()) {
						wrapper.incrementNumeroScrittureDare();
						wrapper.addTotaleDare(importo);
					} else if(elementoScrittura.isSegnoAvere()) {
						wrapper.incrementNumeroScrittureAvere();
						wrapper.addTotaleAvere(importo);
					}
				} else {
					checkCondition(!addErrori,
						ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
							+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
				}
				
			}
		}
		return wrapper;
	}
	
	/**
	 * Conrtolla che non si sia sforato l'importo del movimento di base. In caso contrario, chiede conferma all'utente
	 */
	private void askImportiScritture() {
		// Mi interessa il totale in dare, che e' identico a quello in avere
		ImportiDareAvere wrapper = calcolaImportoDareAvere(model.getListaElementoScritturaPerElaborazione(), false);
		
		List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = model.getListaElementoScritturaPerElaborazione();
		
		// List<ElementoScritturaPrimaNotaIntegrata> scritture
		RegistrazioneMovFin regMovFin = ottieniRegistrazione(listaElementoScritturaPerElaborazione);
		if(regMovFin == null) {			
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessuna registrazione movfin collegata"));
			return;
		}
		
		// Prendo l'importo del movimento
		// NOTA: non posso chiamare il servizio perche' troppo lento. Il dato e' comunque nella registrazione caricata, quindi uso quella
		if(!(regMovFin.getMovimento() instanceof Subdocumento)) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessuna quota collegata alla registrazione"));
			return;
		}
		Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>) regMovFin.getMovimento();
		BigDecimal importoMovimento = subdocumento.getImporto();
		
		warnCondition(importoMovimento == null || importoMovimento.compareTo(wrapper.getTotaleDare()) == 0,
				MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio("il totale del movimento (" + FormatUtils.formatCurrency(importoMovimento)
				+ ") e il totale delle scritture (" + FormatUtils.formatCurrency(wrapper.getTotaleDare()) + ") non coincidono. Proseguire con l'elaborazione?"));
	}

	/**
	 * @param listaElementoScritturaPerElaborazione
	 * @return
	 */
	private RegistrazioneMovFin ottieniRegistrazione(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione) {
		RegistrazioneMovFin regMovFin= estraiRegistrazioneDaListaInElaborazione(listaElementoScritturaPerElaborazione);
		//SIAC-6227
		return regMovFin != null?  regMovFin : model.getRegistrazioneInElaborazione();
	}

	/**
	 * @param listaElementoScritturaPerElaborazione
	 * @return
	 */
	private RegistrazioneMovFin estraiRegistrazioneDaListaInElaborazione(
			List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione) {
		RegistrazioneMovFin regMovFin = null;
		for(Iterator<ElementoScritturaPrimaNotaIntegrata> it = listaElementoScritturaPerElaborazione.iterator(); it.hasNext() && regMovFin == null;) {
			ElementoScritturaPrimaNotaIntegrata espni = it.next();
			RegistrazioneMovFin rmf = espni.retrieveRegistrazioneMovFin();
			if(rmf != null && rmf.getUid() != 0) {
				regMovFin = rmf;
			}
		}
		return regMovFin;
	}
}
