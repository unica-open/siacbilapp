/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.DefinisciPreDocumentoSpesaPerElencoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.ElencoDocumentiAllegatoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe di action per la consultazione del PreDocumento di Spesa
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 30/06/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DefinisciPreDocumentoSpesaPerElencoAction extends GenericPreDocumentoSpesaAction<DefinisciPreDocumentoSpesaPerElencoModel> {
	
	@Autowired
	private transient ElencoDocumentiAllegatoService elencoDocumentiAllegatoService;
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7540742608638012683L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Validate cerca elenco totali.
	 */
	public void validateCercaElencoTotali(){
		ElencoDocumentiAllegato elenco = model.getElencoDocumentiAllegato();
		//controllo che i parametri anno/numero, chiave dell'oggetto elenco siano stati valorizzati
		checkNotNull(elenco.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco").getTesto());
		checkNotNull(elenco.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero elenco").getTesto());
	}
	
	/**
	 * Cerca elenco totali.
	 *
	 * @return the string
	 */
	public String cercaElencoTotali(){
		final String methodName = "cercaElencoTotali";
		RicercaTotaliPredocumentiPerElenco req = model.creaRequestRicercaTotaliPredocumentiPerElenco();
		
		RicercaTotaliPredocumentiPerElencoResponse res = elencoDocumentiAllegatoService.ricercaTotaliPredocumentiPerElenco(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "esecuzione del servizio ricercaTotaliPredocumentiPerElenco terminata con errori.");
			return INPUT;
		}
		model.setElencoDocumentiAllegato(res.getElencoDocumentiAllegato());
		
		model.impostaTotaliElenco(res);
		
		return SUCCESS;
	}
	
	/**
	 * Completa definisci.
	 *
	 * @return the string
	 */
	public String completaDefinisci(){
		final String methodName = "completaDefinisci";
		DefiniscePreDocumentoSpesaPerElenco req = model.creaRequestDefiniscePreDocumentoSpesaPerElenco();
		AsyncServiceResponse response = preDocumentoSpesaService.definiscePreDocumentoSpesaPerElencoAsync(wrapRequestToAsync(req));
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "esecuzione servizio definiscePreDocumentoSpesaPerElencoAsync terminata con errori.");
			addErrori(response);
			return INPUT;
		}
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Definizione predisposizioni per elenco", ". Il risultato sarà disponibile sul cruscotto."));
		return SUCCESS;
	}
	
	/**
	 * Validate completa definisci.
	 */
	public void validateCompletaDefinisci(){
		Impegno movimentoGestione = model.getMovimentoGestione();
		checkNotNullNorInvalidUid(model.getElencoDocumentiAllegato(), "elenco");
		checkCondition(movimentoGestione != null
				&& movimentoGestione.getAnnoMovimento() != 0 
				&& movimentoGestione.getNumero() != null 
				&& movimentoGestione.getNumero().compareTo(BigDecimal.ZERO)>0
				, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("impegno"));
		if(!hasErrori()){
			validazioneImpegnoSubImpegno();
		}
	}


	
}
