/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * Classe di model per la ricerca del PreDocumento di Entrata per il completamento e la definizione
 * 
 * @author Marchino Alessandro
 */
public class CompletaDefinisciPreDocumentoEntrataModel extends BaseCompletaDefinisciPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5080609141854299761L;
	
	
	/** Costruttore vuoto di default */
	public CompletaDefinisciPreDocumentoEntrataModel() {
		setTitolo("Completa e Definisci Predisposizione di Incasso");
	}
	

	/* ***** Requests ***** */


	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPreDocumentoEntrata creaRequestRicercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata request = creaRequest(RicercaSinteticaPreDocumentoEntrata.class);
		
		request.setCompletaDefinisciPreDoc(true);
		
		request.setParametriPaginazione(creaParametriPaginazione(50));
		
		// Flags
		request.setAnnoBilancio(getBilancio().getAnno());
		request.setDataCompetenzaA(getDataCompetenzaA());
		request.setDataCompetenzaDa(getDataCompetenzaDa());
		request.setCausaleEntrataMancante(Boolean.FALSE);
		request.setContoCorrenteMancante(Boolean.FALSE);
		request.setSoggettoMancante(Boolean.FALSE);
		request.setProvvedimentoMancante(Boolean.FALSE);
		request.setEstraiNonIncassato(Boolean.FALSE);
		request.setTipoCausale(getTipoCausale());
		
		//SIAC-6780
//		request.setCausaleEntrata(getCausaleEntrata());
		
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnte());
		
		//SIAC-6780
		preDoc.setContoCorrente(getContoCorrente());
		preDoc.setSoggetto(getSoggetto());
		preDoc.setAccertamento(getAccertamento());
		preDoc.setAttoAmministrativo(getAttoAmministrativo());
		preDoc.setCausaleEntrata(getCausaleEntrata());
		//
		
		request.setPreDocumentoEntrata(preDoc);
		
		request.setDocumento(null);
		
		// SIAC-4383
		request.setDataTrasmissioneDa(null);
		request.setDataTrasmissioneA(null);
		
		// SIAC-4620
		request.setNonAnnullati(null);
		// SIAC-4772
		request.setOrdinativoIncasso(null);
		// SIAC-5250
		request.setOrdinamentoPreDocumentoEntrata(null);
		
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaTotaliPreDocumentoEntrataPerStato}.
	 * @return la request creata
	 */
	public RicercaTotaliPreDocumentoEntrataPerStato creaRequestRicercaTotaliPreDocumentoEntrataPerStato() {
		RicercaTotaliPreDocumentoEntrataPerStato req = creaRequest(RicercaTotaliPreDocumentoEntrataPerStato.class);
		// Parametri di ricerca
		req.setBilancio(getBilancio());
		req.setRequestRicerca(new RicercaSinteticaPreDocumentoEntrata());
		
		PreDocumentoEntrata predoc = new PreDocumentoEntrata();
		predoc.setEnte(getEnte());
		predoc.setCausaleEntrata(impostaEntitaFacoltativa(getCausaleEntrata()));
		predoc.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		
		//SIAC-7443
		predoc.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		req.getRequestRicerca().setPreDocumentoEntrata(predoc);
		req.getRequestRicerca().setTipoCausale(impostaEntitaFacoltativa(getTipoCausale()));;
		req.getRequestRicerca().setDataCompetenzaDa(getDataCompetenzaDa());
		req.getRequestRicerca().setDataCompetenzaA(getDataCompetenzaA());
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link CompletaDefiniscePreDocumentoEntrata}.
	 * @return la request creata
	 */
	public CompletaDefiniscePreDocumentoEntrata creaRequestCompletaDefiniscePreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPredocumentoEntrata = creaRequest(RicercaSinteticaPreDocumentoEntrata.class);
		
		PreDocumentoEntrata predoc = new PreDocumentoEntrata();
		predoc.setEnte(getEnte());
		predoc.setCausaleEntrata(impostaEntitaFacoltativa(getCausaleEntrata()));
		predoc.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		
		ricercaSinteticaPredocumentoEntrata.setPreDocumentoEntrata(predoc);
		
		ricercaSinteticaPredocumentoEntrata.setDataCompetenzaDa(getDataCompetenzaDa());
		ricercaSinteticaPredocumentoEntrata.setDataCompetenzaA(getDataCompetenzaA());
		
		return creaRequestCompletaDefiniscePreDocumentoEntrata(ricercaSinteticaPredocumentoEntrata);
		
//		CompletaDefiniscePreDocumentoEntrata req = creaRequest(CompletaDefiniscePreDocumentoEntrata.class);
//		// Ricerca
//		req.setBilancio(getBilancio());
//		req.setRicercaSinteticaPredocumentoentrata(ricercaSinteticaPredocumentoEntrata);
//		// Aggiornamento
//		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
//		req.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
//		req.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
//		req.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
//		
//		req.setProvvisorioCassa(getProvvisorioCassa());
		
//		return req;
	}

}
