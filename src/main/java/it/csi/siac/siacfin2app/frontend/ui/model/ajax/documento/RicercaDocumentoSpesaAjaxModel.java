/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RicercaDocumentoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;


/**
 * Classe di model per la gestione della ricerca della lista di fatture
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
public class RicercaDocumentoSpesaAjaxModel extends RicercaDocumentoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -387011288099500399L;
	
	private DocumentoSpesa documentoSpesa;
	private Impegno impegno;
	private Boolean collegatoCEC;
	private Boolean contabilizzaGenPcc;
	private BigDecimal importoTotale;
	
	private Integer risultatiPerPagina;
	
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	
	/**
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}

	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the collegatoCEC
	 */
	public Boolean getCollegatoCEC() {
		return collegatoCEC;
	}

	/**
	 * @param collegatoCEC the collegatoCEC to set
	 */
	public void setCollegatoCEC(Boolean collegatoCEC) {
		this.collegatoCEC = collegatoCEC;
	}

	/**
	 * @return the contabilizzaGenPcc
	 */
	public Boolean getContabilizzaGenPcc() {
		return contabilizzaGenPcc;
	}

	/**
	 * @param contabilizzaGenPcc the contabilizzaGenPcc to set
	 */
	public void setContabilizzaGenPcc(Boolean contabilizzaGenPcc) {
		this.contabilizzaGenPcc = contabilizzaGenPcc;
	}

	/**
	 * @return the risultatiPerPagina
	 */
	public Integer getRisultatiPerPagina() {
		return risultatiPerPagina;
	}

	/**
	 * @param risultatiPerPagina the risultatiPerPagina to set
	 */
	public void setRisultatiPerPagina(Integer risultatiPerPagina) {
		this.risultatiPerPagina = risultatiPerPagina;
	}

	/**
	 * @return the listaSubdocumentoSpesa
	 */
	public List<SubdocumentoSpesa> getListaSubdocumentoSpesa() {
		return listaSubdocumentoSpesa;
	}

	/**
	 * @param listaSubdocumentoSpesa the listaSubdocumentoSpesa to set
	 */
	public void setListaSubdocumentoSpesa(List<SubdocumentoSpesa> listaSubdocumentoSpesa) {
		this.listaSubdocumentoSpesa = listaSubdocumentoSpesa != null ? listaSubdocumentoSpesa : new ArrayList<SubdocumentoSpesa>();
	}
	
	/**
	 * @return the importoTotale
	 */
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	/**
	 * @param importoTotale the importoTotale to set
	 */
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	@Override
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto,
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		components.add(super.componiStringaRiepilogo(documento, listaTipoDoc, listaTipoAtto, listaStrutturaAmministrativoContabile));
		if(StringUtils.isNotBlank(getFlagIva())) {
			components.add("Con capitolo rilevante IVA: " + FormatUtils.formatBoolean("S".equals(getFlagIva()), "Si", "No"));
		}
		return StringUtils.join(components, " - ");
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di Ricerca Sintetica Documento Spesa
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoSpesa creaRequestRicercaSinteticaDocumentoSpesa() {
		RicercaSinteticaDocumentoSpesa request = creaRequest(RicercaSinteticaDocumentoSpesa.class);
		
		getDocumentoSpesa().setEnte(getEnte());
		
		request.setDocumentoSpesa(getDocumentoSpesa());
		request.setCollegatoCEC(getCollegatoCEC());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		if(checkPresenzaIdEntita(getImpegno())) {
			request.setImpegno(getImpegno());
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			request.setElencoDocumenti(getElencoDocumenti());
		}
		
		ParametriPaginazione parametriPaginazione = getRisultatiPerPagina() != null
				? creaParametriPaginazione(getRisultatiPerPagina().intValue())
				: creaParametriPaginazione();
		request.setParametriPaginazione(parametriPaginazione);
		return request;
	}

	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoSpesa creaRequestRicercaDocumentoSpesa() {
		RicercaSinteticaDocumentoSpesa request = creaRequest(RicercaSinteticaDocumentoSpesa.class);
		
		getDocumentoSpesa().setEnte(getEnte());
		getDocumentoSpesa().setSoggetto(getSoggetto());
		request.setDocumentoSpesa(getDocumentoSpesa());
		request.setCollegatoCEC(getCollegatoCEC());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		ParametriPaginazione parametriPaginazione = getRisultatiPerPagina() != null
				? creaParametriPaginazione(getRisultatiPerPagina().intValue())
				: creaParametriPaginazione();
		request.setParametriPaginazione(parametriPaginazione);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoSpesa}.
	 * @param documentoSpesaModelDetails i model detail da impostare nel servizio per limitare i dati recuperati
	 * @return la request creata
	 */
	public RicercaSinteticaModulareDocumentoSpesa creaRequestRicercaSinteticaModulareDocumentoSpesa(DocumentoSpesaModelDetail... documentoSpesaModelDetails) {
		RicercaSinteticaModulareDocumentoSpesa request = creaRequest(RicercaSinteticaModulareDocumentoSpesa.class);
		
		getDocumentoSpesa().setEnte(getEnte());
		getDocumentoSpesa().setSoggetto(getSoggetto());
		request.setDocumentoSpesa(getDocumentoSpesa());
		request.setCollegatoCEC(getCollegatoCEC());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		ParametriPaginazione parametriPaginazione = getRisultatiPerPagina() != null
				? creaParametriPaginazione(getRisultatiPerPagina().intValue())
				: creaParametriPaginazione();
		request.setParametriPaginazione(parametriPaginazione);
		
		request.setDocumentoSpesaModelDetails(documentoSpesaModelDetails != null? documentoSpesaModelDetails : DocumentoSpesaModelDetail.values());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.	 
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(){
		RicercaImpegnoPerChiaveOttimizzato request = creaRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		
		RicercaImpegnoK parametroRicercaImpegnoK = new RicercaImpegnoK();
		parametroRicercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
		parametroRicercaImpegnoK.setNumeroImpegno(impegno.getNumero());
		parametroRicercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
		
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setEnte(getEnte());
		request.setpRicercaImpegnoK(parametroRicercaImpegnoK);
		request.setCaricaSub(false);
				
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa() {
		RicercaQuoteByDocumentoSpesa request = creaRequest(RicercaQuoteByDocumentoSpesa.class);
		
		request.setDocumentoSpesa(getDocumentoSpesa());
		
		return request;
	}

	@Override
	protected void componiStringaRiepilogoMovimento(List<String> components) {
		List<String> subComponents = new ArrayList<String>();
		
		if(getImpegno().getAnnoMovimento() != 0) {
			subComponents.add(getImpegno().getAnnoMovimento() + "");
		}
		if(getImpegno().getNumero() != null) {
			subComponents.add(getImpegno().getNumero().toPlainString());
		}
		
		components.add("Movimento: " + StringUtils.join(subComponents, "/"));
	}
}
