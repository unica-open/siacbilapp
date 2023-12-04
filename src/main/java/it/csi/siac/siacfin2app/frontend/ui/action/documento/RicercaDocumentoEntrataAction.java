/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RicercaDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;

/**
 * Classe di Action per la gestione della ricerca del Documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentoEntrataAction extends GenericDocumentoAction<RicercaDocumentoEntrataModel> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;

	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	

	@Override
	public void prepare() throws Exception {
		super.prepare();

		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.ENTRATA, null, null);
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaTipiAtto();
		caricaListaStati();
		caricaListaStatiSDI(); // SIAC-6565
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * Ricerca dei documenti sulla base dei criteri impostati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDocumento() {
		final String methodName = "ricercaDocumento";

		if (!checkIsValidaRicercaDocumento()) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}

		log.debug(methodName, "Effettua la ricerca");

		RicercaSinteticaModulareDocumentoEntrata request = model.creaRequestRicercaSinteticaModulareDocumentoEntrata();
		logServiceRequest(request);

		RicercaSinteticaModulareDocumentoEntrataResponse response = documentoEntrataService.ricercaSinteticaModulareDocumentoEntrata(request);
		logServiceResponse(response);

		
		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		
		if(response.getDocumenti().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Totale: "+response.getDocumenti().getTotaleElementi());
		 
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_ENTRATA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_ENTRATA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);

		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,model.componiStringaRiepilogo(model.getDocumento(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link RicercaDocumentoEntrataAction#ricercaDocumento()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	public boolean checkIsValidaRicercaDocumento() {
		final String methodName = "checkIsValidaRicercaDocumento";
		log.debugStart(methodName, "Verifica campi");
		
		DocumentoEntrata documento = model.getDocumento();
		Boolean formValido = checkPresenzaIdEntita(documento.getTipoDocumento());
		
		if(Boolean.TRUE.equals(formValido)) {
			// Ho il tipo documento. Ne tiro su i dati
			TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), documento.getTipoDocumento());
			documento.setTipoDocumento(tipoDocumento);
		}
		
		formValido = formValido ||
					 checkCampoValorizzato(documento.getStatoOperativoDocumento(), "Stato") ||
					 checkCampoValorizzato(documento.getAnno(), "Anno") ||
					 checkStringaValorizzata(documento.getNumero(), "Numero") ||
					 checkCampoValorizzato(documento.getDataEmissione(), "Data") ||
					 checkStringaValorizzata(model.getFlagIva(), "Iva") ||
					 checkCampoValorizzato(documento.getAnnoRepertorio(), "Anno protocollo") ||
					 checkStringaValorizzata(documento.getNumeroRepertorio(), "Numero protocollo") ||
					 checkCampoValorizzato(documento.getDataRepertorio(), "Data protocollo") ||
					 checkStringaValorizzata(documento.getRegistroRepertorio(), "Registro protocollo") ||
					 checkStringaValorizzata(documento.getStatoSDI(),"stato SDI") ||
					 (model.getPredocumento() != null && checkStringaValorizzata(model.getPredocumento().getNumero(), "numero predisposizione"));

		model.setCampoFormPrincipalePresente(formValido);
						
		boolean movimentoPresente = (model.getAccertamento().getAnnoMovimento() != 0 && Integer.toString(model.getAccertamento().getAnnoMovimento()) != null) ||
																					model.getAccertamento().getNumeroBigDecimal() != null;
		model.setMovimentoPresente(movimentoPresente);
						
						
		// verifica soggetto
		boolean soggettoPresente = model.getSoggetto().getCodiceSoggetto()!= null && !(model.getSoggetto().getCodiceSoggetto().length()==0);
		
		model.setSoggettoPresente(soggettoPresente);
		
		//verifica provvedimento
		boolean provvedimentoPresente = checkProvvedimentoEsistente();
		
		model.setProvvedimentoPresente(Boolean.valueOf(provvedimentoPresente));
		log.debug(methodName, "Provvedimento presente? " + provvedimentoPresente);
		
		boolean elencoPresente = model.getElencoDocumenti() != null && ( model.getElencoDocumenti().getAnno() != null || model.getElencoDocumenti().getNumero() != null);
		if(elencoPresente){
			checkCondition(model.getElencoDocumenti().getAnno() != null && model.getElencoDocumenti().getNumero() != null, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Elenco", ": devono essere entrambi valorizzati o non valorizzati"));
		}
		model.setElencoPresente(elencoPresente);
		
		formValido = ((formValido || soggettoPresente) || provvedimentoPresente) || movimentoPresente || elencoPresente;
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
		
		// Check sul soggetto		
		if(soggettoPresente) {
			validaSoggetto(false);
		}
		if(provvedimentoPresente && checkFormProvvedimentoValido(model.getAttoAmministrativo())) {
			//verificaUnicitaProvvedimento();
			// Popolamento provvedimento
			//siac-5660
			AttoAmministrativo attoAmm = new AttoAmministrativo();
			attoAmm.setUid(model.getUidProvvedimento() != null ? model.getUidProvvedimento() : 0);
			attoAmm.setAnno(model.getAttoAmministrativo().getAnno() );
			attoAmm.setNumero(model.getAttoAmministrativo().getNumero() );
			attoAmm.setTipoAtto(model.getTipoAtto());			
			attoAmm.setStrutturaAmmContabile(model.getStrutturaAmministrativoContabile());		
			model.setAttoAmministrativo(attoAmm);
		}

		// Anno Impegno e numero devono essere entrambi presenti o entrambi assenti
		if(movimentoPresente) {
			if(((model.getAccertamento().getAnnoMovimento() != 0 && Integer.toString(model.getAccertamento().getAnnoMovimento()) != null) && model.getAccertamento().getNumeroBigDecimal() == null) ||
			   ((model.getAccertamento().getAnnoMovimento() == 0 || Integer.toString(model.getAccertamento().getAnnoMovimento()) == null) &&  model.getAccertamento().getNumeroBigDecimal() != null)) {
					addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Movimento", ": devono essere entrambi valorizzati o non valorizzati"));
			} else {
				// sono entrambi valorizzati, verifico che esista l'impegno
				verificaUnicitaAccertamento();
			}
		}
		log.debugEnd(methodName, "");
		return !hasErrori();
	}


	/**
	 * Validazione di unicit&agrave; per accertamento.
	 */
	protected void verificaUnicitaAccertamento() {
		
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			checkCondition(response.getAccertamento() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero","L'accertamento indicato"));
			model.setAccertamento(response.getAccertamento());
		}
	}
	
}
