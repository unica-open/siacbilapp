/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
  *  Classe di model per la ricerca del documento di spesa.
  * 
  * @author Osorio Alessandra
  * @version 1.0.0 - 10/03/2014
  *
  */
public class RicercaDocumentoSpesaModel extends RicercaDocumentoModel {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	private String flagCollegatoCEC;
	
	private Impegno impegno;
	
	private DocumentoSpesa documento;
	
	private List<SubdocumentoSpesa> listaSubDocRicerca = new ArrayList<SubdocumentoSpesa>();
	
	private String flagAttivaScrittureContabili;

	
	/** Costruttore vuoto di default */
	public RicercaDocumentoSpesaModel() {
		setTitolo("Ricerca Documento Spesa");
		setLabelEntrataSpesa("Spesa");
		setLabelRicercaEntrataSpesa("Ricerca Documenti di spesa");
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
	 * @return the flagCollegatoCEC
	 */
	public String getFlagCollegatoCEC() {
		return flagCollegatoCEC;
	}


	/**
	 * @param flagCollegatoCEC the flagCollegatoCEC to set
	 */
	public void setFlagCollegatoCEC(String flagCollegatoCEC) {
		this.flagCollegatoCEC = flagCollegatoCEC;
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
	 * @return the listaSubDocRicerca
	 */
	public List<SubdocumentoSpesa> getListaSubDocRicerca() {
		return listaSubDocRicerca;
	}


	/**
	 * @param listaSubDocRicerca the listaSubDocRicerca to set
	 */
	public void setListaSubDocRicerca(List<SubdocumentoSpesa> listaSubDocRicerca) {
		this.listaSubDocRicerca = listaSubDocRicerca;
	}


	/**
	 * @return the flagAttivaScrittureContabili
	 */
	public String getFlagAttivaScrittureContabili() {
		return flagAttivaScrittureContabili;
	}


	/**
	 * @param flagAttivaScrittureContabili the flagAttivaScrittureContabili to set
	 */
	public void setFlagAttivaScrittureContabili(String flagAttivaScrittureContabili) {
		this.flagAttivaScrittureContabili = flagAttivaScrittureContabili;
	}


	/**
	 * Crea una request per il servizio di Ricerca Sintetica Modulare Documento Spesa
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareDocumentoSpesa creaRequestRicercaSinteticaModulareDocumentoSpesa() {
		RicercaSinteticaModulareDocumentoSpesa request = creaRequest(RicercaSinteticaModulareDocumentoSpesa.class);
		
		documento.setEnte(getEnte());
		
		injettaSoggetto();
			
		request.setDocumentoSpesa(documento);
		request.setRilevanteIva(FormatUtils.parseBooleanSN(getFlagIva()));
		request.setCollegatoCEC(FormatUtils.parseBooleanSN(getFlagCollegatoCEC()));
		request.setContabilizzaGenPcc(FormatUtils.parseBooleanSN(getFlagAttivaScrittureContabili()));
		//if(checkPresenzaIdEntita(getAttoAmministrativo())) {
			request.setAttoAmministrativo(getAttoAmministrativo());
		//}
		if(checkPresenzaIdEntita(getImpegno())) {
			request.setImpegno(getImpegno());
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			request.setElencoDocumenti(getElencoDocumenti());
		}
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setDocumentoSpesaModelDetails(
				DocumentoSpesaModelDetail.Soggetto,
				DocumentoSpesaModelDetail.Stato,
				DocumentoSpesaModelDetail.TipoDocumento);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(){
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		
		request.setEnte(getEnte());
		request.setCaricaSub(false);
		request.setSubPaginati(true);
		
		if(impegno != null) {
			RicercaImpegnoK parametroRicercaImpegnoK = new RicercaImpegnoK();
			parametroRicercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
			parametroRicercaImpegnoK.setNumeroImpegno(impegno.getNumero());
			parametroRicercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
			request.setpRicercaImpegnoK(parametroRicercaImpegnoK);
		}
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);

		return request;
	}
	
	@Override
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		components.add(super.componiStringaRiepilogo(documento, listaTipoDoc, listaTipoAtto, listaStrutturaAmministrativoContabile));
		if(StringUtils.isNotBlank(getFlagIva())) {
			components.add("Con capitolo rilevante IVA: " + FormatUtils.formatBoolean("S".equals(getFlagIva()), "Si", "No"));
		}
		if(StringUtils.isNotBlank(getFlagCollegatoCEC())) {
			components.add("Collegato a cassa economale: " + FormatUtils.formatBoolean("S".equals(getFlagCollegatoCEC()), "Si", "No"));
		}
		return StringUtils.join(components, " - ");
	}

	/**
	 * Metodo di utilit&agrave; per l'iniezione del soggetto nel documento.
	 */
	private void injettaSoggetto() {
		if(!Boolean.TRUE.equals(getSoggettoPresente())) {
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
