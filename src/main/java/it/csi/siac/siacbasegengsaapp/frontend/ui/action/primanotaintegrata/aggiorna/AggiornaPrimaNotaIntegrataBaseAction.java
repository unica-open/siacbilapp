/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.importi.ImportiDareAvere;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegatoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
/**
 * Classe di action per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AggiornaPrimaNotaIntegrataBaseAction<M extends AggiornaPrimaNotaIntegrataBaseModel> extends BaseAggiornaPrimaNotaIntegrataBaseAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 144888717712278061L;
	
	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_FIN = "AggiornaPrimaNotaIntegrataFINModel";

	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_GSA = "AggiornaPrimaNotaIntegrataGSAModel";
	
	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;
	
	@Override
	protected void popolaModel(PrimaNota primaNota) {
		model.setPrimaNota(primaNota);
		MovimentoEP movEP = primaNota.getListaMovimentiEP().get(0);
		model.setCausaleEP(movEP.getCausaleEP());
		model.setRegMovFin(movEP.getRegistrazioneMovFin());
		List<ElementoScritturaPrimaNotaIntegrata> listaElemScritturaPNI = ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaPrimaNota(primaNota);
		model.setListaElementoScrittura(listaElemScritturaPNI);
		model.setListaElementoScritturaPerElaborazione(listaElemScritturaPNI);
		
	}
	
	@Override
	protected void impostaDatiPerPaginazioneDatiFinanziari(OttieniEntitaCollegatePrimaNota req,	OttieniEntitaCollegatePrimaNotaResponse res) {
		//I dati finanziari sono paginati sono nel caso di prima nota afferente ad un documento. Esco senza impostare nulla.
	}
	
	/**
	 * Calcolo dei totali dare e avere.
	 */
	@Override
	protected void calcolaTotali(){
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
	

	@Override
	protected void checkScrittureCorrette() {
		ImportiDareAvere wrapper = calcolaImportoDareAvere(model.getListaElementoScritturaPerElaborazione(), true);
		checkCondition(wrapper.getNumeroScrittureDare() > 0 && wrapper.getNumeroScrittureAvere() > 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
		checkCondition(wrapper.getTotaleDare().subtract(wrapper.getTotaleAvere()).signum() == 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
	}
	
	/**
	 * Calcolo degli importi in dare e avere
	 * @param addErrori se aggiungere gli errori
	 * @return gli importi
	 */
	private ImportiDareAvere calcolaImportoDareAvere(List<ElementoScritturaPrimaNotaIntegrata> scritture, boolean addErrori) {
		ImportiDareAvere wrapper = new ImportiDareAvere();
		
		for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : scritture){
			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
				BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
				
				checkCondition(!addErrori || (elementoScrittura.getMovimentoDettaglio().getConto() != null && elementoScrittura.getMovimentoDettaglio().getConto().getUid() != 0),
						ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("e' necessario assegnare i conti a tutte le scritture"), true);
				if (importo == null){
					checkCondition(!addErrori, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
							+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
					continue;
				}
				
				if(elementoScrittura.isSegnoDare()) {
					wrapper.incrementNumeroScrittureDare();
					wrapper.addTotaleDare(importo);
				}
				
				if(elementoScrittura.isSegnoAvere()) {
					wrapper.incrementNumeroScrittureAvere();
					wrapper.addTotaleAvere(importo);
				}
			}
		}
		return wrapper;
	}
	
	/**
	 * Pass-through per l'aggiornamento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaSuccess() {
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	@Override
	public void validateAggiorna() {
		super.validateAggiorna();
		if(!hasErrori() && !model.isProseguiImportiNonCongruenti()) {
			askImportiScritture();
		}
	}
	
	/**
	 * Conrtolla che non si sia sforato l'importo del movimento di base. In caso contrario, chiede conferma all'utente
	 */
	private void askImportiScritture() {
		// Mi interessa il totale in dare, che e' identico a quello in avere
		ImportiDareAvere wrapper = calcolaImportoDareAvere(model.getListaElementoScritturaPerElaborazione(), false);
		
		// Prendo l'importo del movimento
		CalcolaImportoMovimentoCollegato req = model.creaRequestCalcolaImportoMovimentoCollegato();
		CalcolaImportoMovimentoCollegatoResponse res = registrazioneMovFinService.calcolaImportoMovimentoCollegato(req);
		if(res.hasErrori()) {
			addErrori(res);
			return;
		}
		
		BigDecimal importoMovimento = res.getImporto();
		warnCondition(importoMovimento == null || importoMovimento.compareTo(wrapper.getTotaleDare()) == 0,
				MessaggioBil.MESSAGGIO_DI_SISTEMA.getMessaggio("il totale del movimento (" + FormatUtils.formatCurrency(importoMovimento)
				+ ") e il totale delle scritture (" + FormatUtils.formatCurrency(wrapper.getTotaleDare()) + ") non coincidono. Proseguire con l'elaborazione?"));
	}
	
}
