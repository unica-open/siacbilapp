/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per la ricerca del Documento Iva di Entrata
 * 
 *
 */
public class RicercaDocumentoIvaEntrataModel extends GenericDocumentoIvaEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3398006391781647554L;
	
	private TipoDocumento tipoDocumento;
	private TipoRegistrazioneIva tipoRegistrazioneIva;
	private RegistroIva registroIva;

	private Integer progressivoIvaDa;
	private Integer progressivoIvaA;
	
	private Integer numeroProtocolloProvvisorioDa;
	private Integer numeroProtocolloProvvisorioA;
	private Date dataProtocolloProvvisorioDa;
	private Date dataProtocolloProvvisorioA;
	
	private Integer numeroProtocolloDefinitivoDa;
	private Integer numeroProtocolloDefinitivoA;
	private Date dataProtocolloDefinitivoDa;
	private Date dataProtocolloDefinitivoA;
	
	private String flagIntracomunitario;
	private String flagRilevanteIrap;
	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();

	/** Costruttore vuoto di default */
	public RicercaDocumentoIvaEntrataModel() {
		setTitolo("Ricerca Documenti iva di entrata");
		setFlagIntracomunitarioUtilizzabile(Boolean.TRUE);
	}
	
	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the tipoRegistrazioneIva
	 */
	public TipoRegistrazioneIva getTipoRegistrazioneIva() {
		return tipoRegistrazioneIva;
	}

	/**
	 * @param tipoRegistrazioneIva the tipoRegistrazioneIva to set
	 */
	public void setTipoRegistrazioneIva(TipoRegistrazioneIva tipoRegistrazioneIva) {
		this.tipoRegistrazioneIva = tipoRegistrazioneIva;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @param registroIva the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @return the progressivoIvaDa
	 */
	public Integer getProgressivoIvaDa() {
		return progressivoIvaDa;
	}

	/**
	 * @param progressivoIvaDa the progressivoIvaDa to set
	 */
	public void setProgressivoIvaDa(Integer progressivoIvaDa) {
		this.progressivoIvaDa = progressivoIvaDa;
	}

	/**
	 * @return the progressivoIvaA
	 */
	public Integer getProgressivoIvaA() {
		return progressivoIvaA;
	}

	/**
	 * @param progressivoIvaA the progressivoIvaA to set
	 */
	public void setProgressivoIvaA(Integer progressivoIvaA) {
		this.progressivoIvaA = progressivoIvaA;
	}

	/**
	 * @return the numeroProtocolloProvvisorioDa
	 */
	public Integer getNumeroProtocolloProvvisorioDa() {
		return numeroProtocolloProvvisorioDa;
	}

	/**
	 * @param numeroProtocolloProvvisorioDa the numeroProtocolloProvvisorioDa to set
	 */
	public void setNumeroProtocolloProvvisorioDa(
			Integer numeroProtocolloProvvisorioDa) {
		this.numeroProtocolloProvvisorioDa = numeroProtocolloProvvisorioDa;
	}

	/**
	 * @return the numeroProtocolloProvvisorioA
	 */
	public Integer getNumeroProtocolloProvvisorioA() {
		return numeroProtocolloProvvisorioA;
	}

	/**
	 * @param numeroProtocolloProvvisorioA the numeroProtocolloProvvisorioA to set
	 */
	public void setNumeroProtocolloProvvisorioA(Integer numeroProtocolloProvvisorioA) {
		this.numeroProtocolloProvvisorioA = numeroProtocolloProvvisorioA;
	}

	/**
	 * @return the dataProtocolloProvvisorioDa
	 */
	public Date getDataProtocolloProvvisorioDa() {
		if(dataProtocolloProvvisorioDa == null) {
			return null;
		}
		return new Date(dataProtocolloProvvisorioDa.getTime());
	}

	/**
	 * @param dataProtocolloProvvisorioDa the dataProtocolloProvvisorioDa to set
	 */
	public void setDataProtocolloProvvisorioDa(Date dataProtocolloProvvisorioDa) {
		if(dataProtocolloProvvisorioDa != null) {
			this.dataProtocolloProvvisorioDa = new Date(dataProtocolloProvvisorioDa.getTime());
		} else {
			this.dataProtocolloProvvisorioDa = null;
		}
	}

	/**
	 * @return the dataProtocolloProvvisorioA
	 */
	public Date getDataProtocolloProvvisorioA() {
		if(dataProtocolloProvvisorioA == null) {
			return null;
		}
		return new Date(dataProtocolloProvvisorioA.getTime());
	}

	/**
	 * @param dataProtocolloProvvisorioA the dataProtocolloProvvisorioA to set
	 */
	public void setDataProtocolloProvvisorioA(Date dataProtocolloProvvisorioA) {
		if(dataProtocolloProvvisorioA != null) {
			this.dataProtocolloProvvisorioA = new Date(dataProtocolloProvvisorioA.getTime());
		} else {
			this.dataProtocolloProvvisorioA = null;
		}
	}

	/**
	 * @return the numeroProtocolloDefinitivoDa
	 */
	public Integer getNumeroProtocolloDefinitivoDa() {
		return numeroProtocolloDefinitivoDa;
	}

	/**
	 * @param numeroProtocolloDefinitivoDa the numeroProtocolloDefinitivoDa to set
	 */
	public void setNumeroProtocolloDefinitivoDa(Integer numeroProtocolloDefinitivoDa) {
		this.numeroProtocolloDefinitivoDa = numeroProtocolloDefinitivoDa;
	}

	/**
	 * @return the numeroProtocolloDefinitivoA
	 */
	public Integer getNumeroProtocolloDefinitivoA() {
		return numeroProtocolloDefinitivoA;
	}

	/**
	 * @param numeroProtocolloDefinitivoA the numeroProtocolloDefinitivoA to set
	 */
	public void setNumeroProtocolloDefinitivoA(Integer numeroProtocolloDefinitivoA) {
		this.numeroProtocolloDefinitivoA = numeroProtocolloDefinitivoA;
	}

	/**
	 * @return the dataProtocolloDefinitivoDa
	 */
	public Date getDataProtocolloDefinitivoDa() {
		if(dataProtocolloDefinitivoDa == null) {
			return null;
		}
		return new Date(dataProtocolloDefinitivoDa.getTime());
	}

	/**
	 * @param dataProtocolloDefinitivoDa the dataProtocolloDefinitivoDa to set
	 */
	public void setDataProtocolloDefinitivoDa(Date dataProtocolloDefinitivoDa) {
		if(dataProtocolloDefinitivoDa != null) {
			this.dataProtocolloDefinitivoDa = new Date(dataProtocolloDefinitivoDa.getTime());
		} else {
			this.dataProtocolloDefinitivoDa = null;
		}
	}

	/**
	 * @return the dataProtocolloDefinitivoA
	 */
	public Date getDataProtocolloDefinitivoA() {
		if(dataProtocolloDefinitivoA == null) {
			return null;
		}
		return new Date(dataProtocolloDefinitivoA.getTime());
	}

	/**
	 * @param dataProtocolloDefinitivoA the dataProtocolloDefinitivoA to set
	 */
	public void setDataProtocolloDefinitivoA(Date dataProtocolloDefinitivoA) {
		if(dataProtocolloDefinitivoA != null) {
			this.dataProtocolloDefinitivoA = new Date(dataProtocolloDefinitivoA.getTime());
		} else {
			this.dataProtocolloDefinitivoA = null;
		}
	}

	/**
	 * @return the flagIntracomunitario
	 */
	public String getFlagIntracomunitario() {
		return flagIntracomunitario;
	}

	/**
	 * @param flagIntracomunitario the flagIntracomunitario to set
	 */
	public void setFlagIntracomunitario(String flagIntracomunitario) {
		this.flagIntracomunitario = flagIntracomunitario;
	}

	/**
	 * @return the flagRilevanteIrap
	 */
	public String getFlagRilevanteIrap() {
		return flagRilevanteIrap;
	}

	/**
	 * @param flagRilevanteIrap the flagRilevanteIrap to set
	 */
	public void setFlagRilevanteIrap(String flagRilevanteIrap) {
		this.flagRilevanteIrap = flagRilevanteIrap;
	}

	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaRegistroIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaRegistroIva creaRequestRicercaRegistroIva() {
		RicercaRegistroIva request = creaRequest(RicercaRegistroIva.class);
		
		RegistroIva ri = new RegistroIva();
		ri.setEnte(getEnte());
		ri.setTipoRegistroIva(getTipoRegistroIva());
		
		request.setRegistroIva(ri);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento la famiglia del documento (Entrata / Spesa)
	 * @param flagSubordinato       se il documento &eacute; subordinato
	 * @param flagRegolarizzazione  se il documento &eacute; una regolarizzazione
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setFlagRegolarizzazione(flagRegolarizzazione);
		request.setFlagSubordinato(flagSubordinato);
		request.setRichiedente(getRichiedente());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaSubdocumentoIvaEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaSubdocumentoIvaEntrata creaRequestRicercaSinteticaSubdocumentoIva() {
		RicercaSinteticaSubdocumentoIvaEntrata request = creaRequest(RicercaSinteticaSubdocumentoIvaEntrata.class);
		
		request.setSubdocumentoIvaEntrata(creaSubdocumentoIvaEntrataPerRicerca());
		
		request.setNumeroProtocolloDefinitivoDa(getNumeroProtocolloDefinitivoDa());
		request.setNumeroProtocolloDefinitivoA(getNumeroProtocolloDefinitivoA());
		request.setNumeroProtocolloProvvisorioDa(getNumeroProtocolloProvvisorioDa());
		request.setNumeroProtocolloProvvisorioA(getNumeroProtocolloProvvisorioA());
		
		request.setProtocolloDefinitivoDa(getDataProtocolloDefinitivoDa());
		request.setProtocolloDefinitivoA(getDataProtocolloDefinitivoA());
		request.setProtocolloProvvisorioDa(getDataProtocolloProvvisorioDa());
		request.setProtocolloProvvisorioA(getDataProtocolloProvvisorioA());
		
		request.setProgressivoIVAA(getProgressivoIvaA());
		request.setProgressivoIVADa(getProgressivoIvaDa());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	/**
	 * Crea un subdocumento iva per la ricerca sintetica.
	 * 
	 * @return il subdocumento iva creato e popolato
	 */
	private SubdocumentoIvaEntrata creaSubdocumentoIvaEntrataPerRicerca() {
		SubdocumentoIvaEntrata sis = getSubdocumentoIva();
		if(sis == null) {
			sis = new SubdocumentoIvaEntrata();
		}
		
		// Popolo il registro
		if(getRegistroIva() == null) {
			setRegistroIva(new RegistroIva());
		}
		getRegistroIva().setTipoRegistroIva(getTipoRegistroIva());
		
		// Popolo il documento
		if(getDocumento() == null) {
			// Non dovrebbe succedere, ma meglio prevenire
			setDocumento(new DocumentoEntrata());
		}
		getDocumento().setTipoDocumento(impostaEntitaFacoltativa(getTipoDocumento()));
		getDocumento().setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		
		// Imposto il subdocumento di spesa
		sis.setEnte(getEnte());
		sis.setDocumento(getDocumento());
		sis.setRegistroIva(getRegistroIva());
		sis.setTipoRegistrazioneIva(impostaEntitaFacoltativa(getTipoRegistrazioneIva()));
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		// Popolo i flag
		sis.setFlagIntracomunitario(FormatUtils.parseBooleanSN(getFlagIntracomunitario()));
		sis.setFlagRilevanteIRAP(FormatUtils.parseBooleanSN(getFlagRilevanteIrap()));
		
		return sis;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param sogg il soggetto da ricercare
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto sogg) {
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		
		request.setParametroSoggettoK(parametroRicercaSoggettoK);
		return request;
	}
	
}
