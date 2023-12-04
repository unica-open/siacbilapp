/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per l'aggiornamento del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/04/2014
 *
 */
public class AggiornaPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6695121258199378514L;
	
	private Integer uidPreDocumentoDaAggiornare;
	private boolean utenteDecentrato;
	
	//SIAC-6780
	private int savedDisplayStart;
	
	private DocumentoEntrata documento;
	private SubdocumentoEntrata subdocumento;

	private Integer uidSubDocumentoDaAssociare;
	
	private boolean proseguireConElaborazione = Boolean.FALSE;
	private boolean fromCompletaDefinisci = Boolean.FALSE;
	//
	
	/** Costruttore vuoto di default */
	public AggiornaPreDocumentoEntrataModel() {
		setTitolo("Aggiornamento Predisposizione di Incasso");
		setNomeAzioneDecentrata(BilConstants.AGGIORNA_PREDOCUMENTO_ENTRATA_DECENTRATO.getConstant());
	}

	/**
	 * @return the uidPreDocumentoDaAggiornare
	 */
	public Integer getUidPreDocumentoDaAggiornare() {
		return uidPreDocumentoDaAggiornare;
	}

	/**
	 * @param uidPreDocumentoDaAggiornare the uidPreDocumentoDaAggiornare to set
	 */
	public void setUidPreDocumentoDaAggiornare(Integer uidPreDocumentoDaAggiornare) {
		this.uidPreDocumentoDaAggiornare = uidPreDocumentoDaAggiornare;
	}

	/**
	 * @return the utenteDecentrato
	 */
	public boolean isUtenteDecentrato() {
		return utenteDecentrato;
	}
	
	/**
	 * @param utenteDecentrato the utenteDecentrato to set
	 */
	public void setUtenteDecentrato(boolean utenteDecentrato) {
		this.utenteDecentrato = utenteDecentrato;
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}
	
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}
	
	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}
	
	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @param the uidSubDocumentoDaAssociare
	 */
	public Integer getUidSubDocumentoDaAssociare() {
		return uidSubDocumentoDaAssociare;
	}
	
	/**
	 * @param uidSubDocumentoDaAssociare the uidSubDocumentoDaAssociare to set
	 */
	public void setUidSubDocumentoDaAssociare(Integer uidSubDocumentoDaAssociare) {
		this.uidSubDocumentoDaAssociare = uidSubDocumentoDaAssociare;
	}

	/**
	 * @return the subdocumento
	 */
	public SubdocumentoEntrata getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SubdocumentoEntrata subdocumento) {
		this.subdocumento = subdocumento;
	}
	
	/**
	 * @return the proseguireConElaborazione
	 */
	public boolean isProseguireConElaborazione() {
		return proseguireConElaborazione;
	}
	
	/**
	 * @param proseguireConElaborazione the proseguireConElaborazione to set
	 */
	public void setProseguireConElaborazione(boolean proseguireConElaborazione) {
		this.proseguireConElaborazione = proseguireConElaborazione;
	}
	
	/**
	 * @return the fromCompletaDefinisci
	 */
	public boolean isFromCompletaDefinisci() {
		return fromCompletaDefinisci;
	}
	
	/**
	 * @param fromCompletaDefinisci the fromCompletaDefinisci to set
	 */
	public void setFromCompletaDefinisci(boolean fromCompletaDefinisci) {
		this.fromCompletaDefinisci = fromCompletaDefinisci;
	}
	
	
	/* ***** Requests ***** */




	/**
	 * Crea una request per il servizio di {@link AggiornaPreDocumentoDiEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPreDocumentoDiEntrata creaRequestAggiornaPreDocumentoDiEntrata() {
		AggiornaPreDocumentoDiEntrata request = new AggiornaPreDocumentoDiEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = getPreDocumento();
		
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumento.setCausaleEntrata(getCausaleEntrata());
		
		preDocumento.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		preDocumento.setProvvisorioDiCassa(getProvvisorioCassa());
		
		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		request.setPreDocumentoEntrata(preDocumento);
		request.setBilancio(getBilancio());
		request.setGestisciModificaImportoAccertamento(getForzaDisponibilitaAccertamento());
		
		//SIAC-6780
		Soggetto sogg = new Soggetto();
		if(getDocumento() != null && getDocumento().getSoggetto() != null && getDocumento().getSoggetto().getCodiceSoggetto() != null) {
			sogg.setCodiceSoggetto(getDocumento().getSoggetto().getCodiceSoggetto());			
		}
		
		TipoDocumento tipo = new TipoDocumento();
		if(getDocumento() != null && getDocumento().getTipoDocumento() != null && getDocumento().getTipoDocumento().getUid() != 0) {
			tipo.setUid(getDocumento().getTipoDocumento().getUid());
		}
		
		DocumentoEntrata doc = new DocumentoEntrata();
		if(getDocumento() != null && getDocumento().getAnno() != null) {
			doc.setAnno(getDocumento().getAnno());
		}
		if(getDocumento() != null && getDocumento().getNumero() != null) {
			doc.setNumero(getDocumento().getNumero());
		}
		doc.setSoggetto(sogg);
		doc.setTipoDocumento(tipo);
		
		SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
		subdoc.setDocumento(doc);
		
		preDocumento.setSubDocumento(subdoc);
		//
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPreDocumentoEntrata creaRequestRicercaDettaglioPreDocumentoEntrata() {
		RicercaDettaglioPreDocumentoEntrata request = new RicercaDettaglioPreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = new PreDocumentoEntrata();
		preDocumento.setUid(getUidPreDocumentoDaAggiornare() != null ? getUidPreDocumentoDaAggiornare() : getPreDocumento().getUid());
		request.setPreDocumentoEntrata(preDocumento);
		
		return request;
	}

	/**
	 * Imposta i dati del PreDocumento di Spesa all'interno del model.
	 *  
	 * @param preDocumentoSpesa il predocumento i cui dati devono essere impostati
	 */
	public void impostaPreDocumento(PreDocumentoEntrata preDocumentoSpesa) {
		setPreDocumento(preDocumentoSpesa);
		setCapitolo(preDocumentoSpesa.getCapitoloEntrataGestione());
		setSoggetto(preDocumentoSpesa.getSoggetto());
		setMovimentoGestione(preDocumentoSpesa.getAccertamento());
		setSubMovimentoGestione(preDocumentoSpesa.getSubAccertamento());
		
		setDatiAnagraficiPreDocumento(preDocumentoSpesa.getDatiAnagraficiPreDocumento());
		setCausaleEntrata(preDocumentoSpesa.getCausaleEntrata());
		setContoCorrente(preDocumentoSpesa.getContoCorrente());
		setStrutturaAmministrativoContabile(preDocumentoSpesa.getStrutturaAmministrativoContabile());
		setTipoCausale(getCausaleEntrata().getTipoCausale());
		
		impostaAttoAmministrativo(preDocumentoSpesa.getAttoAmministrativo());
		
		// SIAC-4492
		setCausaleOriginale(preDocumentoSpesa.getCausaleEntrata());
	}

	public RicercaSinteticaModulareDocumentoEntrata creaRequestRicercaSinteticaModulareDocumentoEntrataPerCollegaDocumento() {
		RicercaSinteticaModulareDocumentoEntrata ricerca = creaRequest(RicercaSinteticaModulareDocumentoEntrata.class);
		DocumentoEntrata doc = new DocumentoEntrata();
		Ente ente = getEnte();
		
		doc.setEnte(ente);
		if(getDocumento() != null && getDocumento().getAnno() != null) {
			doc.setAnno(getDocumento().getAnno());
		}
		if(getDocumento() != null && getDocumento().getNumero() != null) {
			doc.setNumero(getDocumento().getNumero());
		}

		Soggetto sogg = new Soggetto();
		if(getDocumento() != null && getDocumento().getSoggetto() != null && getDocumento().getSoggetto().getCodiceSoggetto() != null) {
			sogg.setCodiceSoggetto(getDocumento().getSoggetto().getCodiceSoggetto());
		}
		
		doc.setSoggetto(sogg);
		
		TipoDocumento tipo = new TipoDocumento();
		if(getDocumento() != null && getDocumento().getTipoDocumento() != null && getDocumento().getTipoDocumento().getUid() != 0) {
			tipo.setUid(getDocumento().getTipoDocumento().getUid());
		}
		tipo.setTipoFamigliaDocumento(TipoFamigliaDocumento.ENTRATA);
		
		doc.setTipoDocumento(tipo);
		
		ricerca.setDocumentoEntrata(doc);
		
		ricerca.setParametriPaginazione(creaParametriPaginazione());
		ricerca.setDocumentoEntrataModelDetails(
				DocumentoEntrataModelDetail.Sogg,
				DocumentoEntrataModelDetail.Stato,
				DocumentoEntrataModelDetail.TipoDocumento);
		
		return ricerca;
	}

	public RicercaQuoteDaAssociarePredocumento creaRequestRicercaQuoteDaAssociarePerCollegaDocumento() {
		RicercaQuoteDaAssociarePredocumento request = creaRequest(RicercaQuoteDaAssociarePredocumento.class);
		ParametriPaginazione paginazione = new ParametriPaginazione();
		TipoDocumento tipo = new TipoDocumento();
		Soggetto sogg = new Soggetto();
		
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		
		if(getDocumento() != null && getDocumento().getAnno() != null) {
			request.setAnnoDocumento(getDocumento().getAnno());
		}
		if(getDocumento() != null && getDocumento().getNumero() != null) {
			request.setNumeroDocumento(getDocumento().getNumero());
		}
		if(getPreDocumento() != null && getPreDocumento().getImporto() != null) {
			request.setImportoPerRicercaQuote(getPreDocumento().getImporto());
		}

		if(getDocumento() != null && getDocumento().getSoggetto() != null && StringUtils.isNotBlank(getDocumento().getSoggetto().getCodiceSoggetto())) {
			sogg.setCodiceSoggetto(getDocumento().getSoggetto().getCodiceSoggetto());
		}
		
		if(getDocumento() != null && getDocumento().getTipoDocumento() != null && getDocumento().getTipoDocumento().getUid() != 0) {
			tipo.setUid(getDocumento().getTipoDocumento().getUid());
		}
		
		/*
		if(getPreDocumento() != null && getPreDocumento().getProvvisorioDiCassa() != null && getPreDocumento().getProvvisorioDiCassa().getNumero() != null && getPreDocumento().getProvvisorioDiCassa().getAnno()!= null) {
			
			request.setNumeroProvvisorio(getPreDocumento().getProvvisorioDiCassa().getNumero());
			//questo lo tengo per passare i controlli
			request.setNumeroProvvisorio(getPreDocumento().getProvvisorioDiCassa().getNumero());
			//
			request.setNumeroProvvisorioCassaRicercaQuote(new BigDecimal(getPreDocumento().getProvvisorioDiCassa().getNumero()));
		}*/
		
		tipo.setTipoFamigliaDocumento(TipoFamigliaDocumento.ENTRATA);
		
		paginazione.setElementiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		
		request.setSoggetto(sogg);
		request.setTipoDocumento(tipo);
		request.setParametriPaginazione(paginazione);
		request.setTipoFamigliaDocumento(TipoFamigliaDocumento.ENTRATA);
		
		List<StatoOperativoDocumento> listaStatiOperativi = new ArrayList<StatoOperativoDocumento>();
		listaStatiOperativi.add(StatoOperativoDocumento.INCOMPLETO);
		listaStatiOperativi.add(StatoOperativoDocumento.ANNULLATO);
		listaStatiOperativi.add(StatoOperativoDocumento.EMESSO);
		
		request.setStatiOperativoDocumento(listaStatiOperativi);
		request.setRicercaPerAssociaPreDoc(Boolean.TRUE);
		
		return request;
	}

	public RicercaDettaglioQuotaEntrata creaRequestRicercaDettaglioQuoteDocumentoEntrata() {
		RicercaDettaglioQuotaEntrata request = creaRequest(RicercaDettaglioQuotaEntrata.class);
		SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
		
		subdoc.setUid(getUidSubDocumentoDaAssociare());
		
		request.setSubdocumentoEntrata(subdoc);
		
		return request;
	}

	public AggiornaPreDocumentoEntrataCollegaDocumento creaRequestCollegaDocumento() {
		AggiornaPreDocumentoEntrataCollegaDocumento request = creaRequest(AggiornaPreDocumentoEntrataCollegaDocumento.class);
		
		request.setBilancio(getBilancio());
		request.setRichiedente(getRichiedente());
		
		request.setPreDocumentoEntrata(getPreDocumento());
		SubdocumentoEntrata se = new SubdocumentoEntrata();
		se.setUid(getUidSubDocumentoDaAssociare());
		request.setSubDocumentoEntrata(se);
		
		return request;
	}

}
