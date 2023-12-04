/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.BaseCompletaDefinisciPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class BaseCompletaDefinisciPreDocumentoEntrataAction<M extends BaseCompletaDefinisciPreDocumentoEntrataModel> extends GenericPreDocumentoEntrataAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -116756292749931469L;

	/**
	 * Validazione per il completamento e la definizione del preDocumento.
	 */
	public void validateCompletaDefinisci() {
		checkAttoAmministrativo();
		checkAccertamento();
		checkSoggetto();
		checkCoerenzaSoggettoAccertamento();
		checkProvvisorio();
	}
	
	private void checkProvvisorio() {
		ProvvisorioDiCassa p = model.getProvvisorioCassa();
		checkCondition(p == null ||  
				(p.getAnno() != null && p.getNumero() != null) ||
				(p.getAnno() == null && p.getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
	}

	/**
	 * Controllo del provvedimento
	 */
	private void checkAttoAmministrativo() {
		// richiamo il metodo di validazione
		validazioneAttoAmministrativo();
		
		AttoAmministrativo aa = model.getAttoAmministrativo();
		if(aa != null && aa.getUid() != 0) {
			model.setTipoAtto(aa.getTipoAtto());
			model.setStrutturaAmministrativoContabileAttoAmministrativo(aa.getStrutturaAmmContabile());
		}
	}

	/**
	 * Controllo di esistenza dell'accertamento
	 */
	private void checkAccertamento() {
		Accertamento accertamento = model.getMovimentoGestione();
		checkCondition(accertamento != null && accertamento.getAnnoMovimento() != 0 && accertamento.getNumeroBigDecimal() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"));
		validazioneAccertamentoSubAccertamento(null);
	}

	
	@Override
	protected void checkDisponibilitaAccertamentoSubaccertamento(){
		//DA LASCIARE VUOTO!!
	}
	/**
	 * Controllo di esistenza del soggetto
	 */
	private void checkSoggetto() {
		Soggetto soggetto = model.getSoggetto();
		checkCondition(soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
		validazioneSoggetto();
	}

	/**
	 * Controllo di coerenza tra accertamento e soggetto
	 */
	private void checkCoerenzaSoggettoAccertamento() {
		controlloConguenzaSoggettoMovimentoGestione(model.getSoggetto(), model.getMovimentoGestione(), model.getSubMovimentoGestione(),
				"predisposizione di incasso", "accertamento");
	}

	
	/**
	 * Completa e definisce
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String completaDefinisci() {
		final String methodName = "completaDefinisci";
		RicercaSinteticaPreDocumentoEntrata reqRicerca = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_PREDOCUMENTI_SELEZIONATI_COMPLETA_DEFINISCI,RicercaSinteticaPreDocumentoEntrata.class);			
		
		CompletaDefiniscePreDocumentoEntrata req = model.creaRequestCompletaDefiniscePreDocumentoEntrata(reqRicerca);
		AsyncServiceResponse res = preDocumentoEntrataService.completaDefiniscePreDocumentoEntrataAsync(wrapRequestToAsync(req));
		
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio di ricerca sintetica");
			addErrori(res);
			return INPUT;
		}
		
		model.setCompletaDefinisciAbilitato(false);
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("completamento e definizione della predisposizione di incasso", ""));
		return SUCCESS;
	}
	
	/**
	 * Imposta totali elenco.
	 *
	 * @param res la response da cui ottenere i dati
	 */
	public void impostaTotaliElenco(RicercaTotaliPreDocumentoEntrataPerStatoResponse res) {
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = res.getImportiPreDocumenti();
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = res.getNumeroPreDocumenti();

		// Importi e numeri...
		
		// Incompleto
		model.setNumeroPreDocumentiIncompleti(defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO)));
		model.setImportoPreDocumentiIncompleti(defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO)));
		// Completi
		model.setNumeroPreDocumentiCompleti(defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.COMPLETO)));
		model.setImportoPreDocumentiCompleti(defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.COMPLETO)));
		// Annullati e definiti
		model.setNumeroPreDocumentiAnnullatiDefiniti(numeroPerStati(numeroPreDocumenti, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		model.setImportoPreDocumentiAnnullatiDefiniti(importoPerStati(importiPreDocumenti, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		// Totale
		model.setImportoPreDocumentiTotale(importoPerStati(importiPreDocumenti, StatoOperativoPreDocumento.values()));
		model.setNumeroPreDocumentiTotale(numeroPerStati(numeroPreDocumenti, StatoOperativoPreDocumento.values()));
		//
	}
	
	/**
	 * Imposta totali elenco senza provvisorio.
	 *
	 * @param res la response da cui ottenere i dati
	 */
	public void impostaTotaliElencoNoCassa(RicercaTotaliPreDocumentoEntrataPerStatoResponse res) {
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumentiNoCassa = res.getImportiPreDocumentiNoCassa();
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumentiNoCassa = res.getNumeroPreDocumentiNoCassa();
		
		// Importi e numeri...
		
		// Incompleto
		model.setNumeroPreDocumentiNoCassaIncompleti(defaultToZero(numeroPreDocumentiNoCassa.get(StatoOperativoPreDocumento.INCOMPLETO)));
		model.setImportoPreDocumentiNoCassaIncompleti(defaultToZero(importiPreDocumentiNoCassa.get(StatoOperativoPreDocumento.INCOMPLETO)));
		// Completi
		model.setNumeroPreDocumentiNoCassaCompleti(defaultToZero(numeroPreDocumentiNoCassa.get(StatoOperativoPreDocumento.COMPLETO)));
		model.setImportoPreDocumentiNoCassaCompleti(defaultToZero(importiPreDocumentiNoCassa.get(StatoOperativoPreDocumento.COMPLETO)));
		// Annullati e definiti
		model.setNumeroPreDocumentiNoCassaAnnullatiDefiniti(numeroPerStati(numeroPreDocumentiNoCassa, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		model.setImportoPreDocumentiNoCassaAnnullatiDefiniti(importoPerStati(importiPreDocumentiNoCassa, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		// Totale
		model.setNumeroPreDocumentiNoCassaTotale(numeroPerStati(numeroPreDocumentiNoCassa, StatoOperativoPreDocumento.values()));
		model.setImportoPreDocumentiNoCassaTotale(importoPerStati(importiPreDocumentiNoCassa, StatoOperativoPreDocumento.values()));
		//
	}
	
	/**
	 * Importo dei predoc per gli stati forniti
	 * @param importiPreDocumenti gli importi
	 * @param stati gli stati
	 * @return gli importi
	 */
	protected BigDecimal importoPerStati(Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti, StatoOperativoPreDocumento... stati) {
		BigDecimal importoTotale = BigDecimal.ZERO;
		Collection<StatoOperativoPreDocumento> statiToCheck = Arrays.asList(stati);
		for(Entry<StatoOperativoPreDocumento, BigDecimal> entry : importiPreDocumenti.entrySet()) {
			if(statiToCheck.contains(entry.getKey())) {
				importoTotale = importoTotale.add(defaultToZero(entry.getValue()));
			}
		}
		return importoTotale;
	}
	
	/**
	 * Numero dei predoc per gli stati forniti
	 * @param numeroPreDocumenti il numero
	 * @param stati gli stati
	 * @return il numero
	 */
	protected Long numeroPerStati(Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti, StatoOperativoPreDocumento... stati) {
		long numeroPredoc = 0;
		Collection<StatoOperativoPreDocumento> statiToCheck = Arrays.asList(stati);
		for(Entry<StatoOperativoPreDocumento, Long> entry : numeroPreDocumenti.entrySet()) {
			if(statiToCheck.contains(entry.getKey())) {
				numeroPredoc += defaultToZero(entry.getValue()).longValue();
			}
		}
		return Long.valueOf(numeroPredoc);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	protected Long defaultToZero(Long value) {
		return value != null ? value : Long.valueOf(0L);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	protected BigDecimal defaultToZero(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}
	
	/**
	 * Validazione per la ricerca dei totali
	 */
	public void validateCercaTotali() {
		boolean formValido =
				checkCampoValorizzato(model.getDataCompetenzaDa(), "Data da") ||
				checkCampoValorizzato(model.getDataCompetenzaA(), "Data a") ||
				checkPresenzaIdEntita(model.getCausaleEntrata()) ||
				checkPresenzaIdEntita(model.getContoCorrente());
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	
	
	
}
