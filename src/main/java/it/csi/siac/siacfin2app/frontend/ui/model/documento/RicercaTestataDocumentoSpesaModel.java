/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe di model per la ricerca della testata del documento di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/07/2014
 */
public class RicercaTestataDocumentoSpesaModel extends RicercaDocumentoModel {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = -7749978301074409354L;
	
	private Impegno impegno;
	private DocumentoSpesa documento;
	
	/** Costruttore vuoto di default */
	public RicercaTestataDocumentoSpesaModel() {
		setTitolo("Ricerca Documento iva Spesa");
		setLabelEntrataSpesa("Spesa");
		setLabelRicercaEntrataSpesa("Ricerca Documenti iva spesa");
		setLabelRilevanteIvaEntrataSpesa("Con capitolo rilevante IVA");
	}
	
	/**
	 * @return the documento
	 */
	public DocumentoSpesa getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoSpesa documento) {
		this.documento = documento;
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

	/* **** Request **** */
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoSpesa creaRequestRicercaSinteticaDocumentoSpesa() {
		RicercaSinteticaDocumentoSpesa request = creaRequest(RicercaSinteticaDocumentoSpesa.class);
		
		getDocumento().setEnte(getEnte());
		
		injettaSoggetto();
			
		request.setDocumentoSpesa(getDocumento());
		request.setRilevanteIva(FormatUtils.parseBooleanSN(getFlagIva()));
		
		request.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		request.setImpegno(impostaEntitaFacoltativa(getImpegno()));
		
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	
	@Override
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		components.add(super.componiStringaRiepilogo(documento, listaTipoDoc, listaTipoAtto, listaStrutturaAmministrativoContabile));
		if(StringUtils.isNotBlank(getFlagIva())) {
			components.add("Con capitolo rilevante IVA: " + FormatUtils.formatBoolean("S".equals(getFlagIva()), "Si", "No"));
		}
		return StringUtils.join(components, " - ");
	}

	/**
	 * Metodo di utilit&agrave; per l'iniezione del soggetto nel documento.
	 */
	private void injettaSoggetto() {
		if(!getSoggettoPresente()) {
			return;
		}
		documento.setSoggetto(getSoggetto());
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
