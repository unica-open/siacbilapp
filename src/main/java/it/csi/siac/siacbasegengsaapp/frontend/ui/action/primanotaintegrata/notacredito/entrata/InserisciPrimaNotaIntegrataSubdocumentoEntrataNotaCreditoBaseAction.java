/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.entrata;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.InserisciPrimaNotaIntegrataSubdocumentoNotaCreditoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata.InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per la validazione della prima integrata per il subdocumento di entrata di una nota credito.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataSubdocumentoEntrataNotaCreditoBaseAction<M extends InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel>
		extends InserisciPrimaNotaIntegrataSubdocumentoNotaCreditoBaseAction<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ElementoQuotaEntrataRegistrazioneMovFin, ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3296610811952891963L;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	protected void impostazioneDocumento() throws WebServiceInvocationFailureException {
		RegistrazioneMovFin registrazioneMovFin = model.getRegistrazioneMovFin();
		SubdocumentoEntrata subdocumentoEntrata = (SubdocumentoEntrata) registrazioneMovFin.getMovimento();
		
		if(subdocumentoEntrata == null || subdocumentoEntrata.getDocumento() == null) {
			throw new WebServiceInvocationFailureException("Errore nel caricamento del movimento: dati non reperiti correttamente");
		}
		
		model.setDocumento(subdocumentoEntrata.getDocumento());
	}
	
	@Override
	protected void ottieniDettaglioSubdocumento() throws WebServiceInvocationFailureException {
		
		// Non vorrei ci sia un problema con Struts2. Per sicurezza, ricasto io.
		if(!(model.getSubdocumento() instanceof SubdocumentoEntrata)) {
			SubdocumentoEntrata subdocumento = new SubdocumentoEntrata();
			subdocumento.setUid(model.getUidSubdocumento());
			model.setSubdocumento(subdocumento);
		}
		
		RicercaDettaglioQuotaEntrata request = model.creaRequestRicercaDettaglioQuotaEntrata();
		logServiceRequest(request);
		RicercaDettaglioQuotaEntrataResponse response = documentoEntrataService.ricercaDettaglioQuotaEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioQuotaEntrata.class, response));
		}
		if(response.getSubdocumentoEntrata() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento", "uid " + model.getSubdocumento().getUid()));
			throw new WebServiceInvocationFailureException("Subdocumento con uid " + model.getSubdocumento().getUid() + " non presente");
		}
		SubdocumentoEntrata subdocumento = response.getSubdocumentoEntrata();
		model.setSubdocumento(subdocumento);
		model.setClassificatoreGerarchico(subdocumento.getAccertamento() == null ? null : subdocumento.getAccertamento().getCapitoloEntrataGestione().getCategoriaTipologiaTitolo());
	}
	
	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getSubdocumento() != null
			&& model.getSubdocumento().getAccertamento() != null
			&& model.getSubdocumento().getAccertamento().getCapitoloEntrataGestione() != null
				? model.getSubdocumento().getAccertamento().getCapitoloEntrataGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getDocumento() != null ? model.getDocumento().getSoggetto() : null;
	}

	@Override
	protected void setImportoInElementoScrittura(BigDecimal imponibile, BigDecimal imposta, ElementoScritturaPrimaNotaIntegrata elementoScrittura) {
		// JIRA-3559: Inverso rispetto al subdoc di stesso segno
		if(elementoScrittura.isSegnoAvere()) {
			// L'avere ha importo pari a imponibile + imposta
			elementoScrittura.getMovimentoDettaglio().setImporto(imponibile.add(imposta));
		} else {
			if(elementoScrittura.isTipoImportoImponibile()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(imponibile);
			} else if(elementoScrittura.isTipoImportoImposta()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(imposta);
			} else if(elementoScrittura.isTipoImportoLordo()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(imponibile.add(imposta).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode()));
			}
		}
	}
}
