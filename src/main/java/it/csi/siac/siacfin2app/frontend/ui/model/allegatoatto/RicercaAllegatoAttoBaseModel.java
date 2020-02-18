/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.MovimentoGestioneHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per la ricerca dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/set/2014
 * @author elisa
 * @version 2.0.0 - 26/feb/2018
 *
 */
public class RicercaAllegatoAttoBaseModel extends GenericAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2664282610561646796L;
	
	
	private Date dataScadenzaDa;
	private Date dataScadenzaA;
	private Soggetto soggetto;
	private Impegno impegno;
	private SubImpegno subImpegno;
	
	private List<StatoOperativoAllegatoAtto> listaStatoOperativoAllegatoAtto = new ArrayList<StatoOperativoAllegatoAtto>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	// CR-4276
	private String flagRitenute;
	
	//SIAC-5584
	private String nomeAzioneRicerca;
	private boolean disabilitaSelezioneStato;
	private boolean convalida;
	
	// SIAC-6166
	private Integer annoBilancio;
	//SIAC-6261
	private String flagSoggettoDurc;
	
	/**
	 * @return the dataScadenzaDa
	 */
	public Date getDataScadenzaDa() {
		return dataScadenzaDa == null ? null : new Date(dataScadenzaDa.getTime());
	}

	/**
	 * @param dataScadenzaDa the dataScadenzaDa to set
	 */
	public void setDataScadenzaDa(Date dataScadenzaDa) {
		this.dataScadenzaDa = dataScadenzaDa == null ? null : new Date(dataScadenzaDa.getTime());
	}

	/**
	 * @return the dataScadenzaA
	 */
	public Date getDataScadenzaA() {
		return dataScadenzaA == null ? null : new Date(dataScadenzaA.getTime());
	}

	/**
	 * @param dataScadenzaA the dataScadenzaA to set
	 */
	public void setDataScadenzaA(Date dataScadenzaA) {
		this.dataScadenzaA = dataScadenzaA == null ? null : new Date(dataScadenzaA.getTime());
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
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
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the listaStatoOperativoAllegatoAtto
	 */
	public List<StatoOperativoAllegatoAtto> getListaStatoOperativoAllegatoAtto() {
		return listaStatoOperativoAllegatoAtto;
	}

	/**
	 * @param listaStatoOperativoAllegatoAtto the listaStatoOperativoAllegatoAtto to set
	 */
	public void setListaStatoOperativoAllegatoAtto(List<StatoOperativoAllegatoAtto> listaStatoOperativoAllegatoAtto) {
		this.listaStatoOperativoAllegatoAtto = listaStatoOperativoAllegatoAtto != null ? listaStatoOperativoAllegatoAtto : new ArrayList<StatoOperativoAllegatoAtto>();
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
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}
	
	/**
	 * @return the flagRitenute
	 */
	public String getFlagRitenute() {
		return flagRitenute;
	}

	/**
	 * @param flagRitenute the flagRitenute to set
	 */
	public void setFlagRitenute(String flagRitenute) {
		this.flagRitenute = flagRitenute;
	}
	/**
	 * @return the nomeAzioneRicerca
	 */
	public String getNomeAzioneRicerca() {
		return nomeAzioneRicerca;
	}

	/**
	 * @param nomeAzioneRicerca the nomeAzioneRicerca to set
	 */
	public void setNomeAzioneRicerca(String nomeAzioneRicerca) {
		this.nomeAzioneRicerca = nomeAzioneRicerca;
	}
	
	/**
	 * @return the disabilitaSelezioneStato
	 */
	public boolean isDisabilitaSelezioneStato() {
		return disabilitaSelezioneStato;
	}

	/**
	 * @param disabilitaSelezioneStato the disabilitaSelezioneStato to set
	 */
	public void setDisabilitaSelezioneStato(boolean disabilitaSelezioneStato) {
		this.disabilitaSelezioneStato = disabilitaSelezioneStato;
	}

	/**
	 * Checks if is convalida.
	 *
	 * @return the convalida
	 */
	public boolean isConvalida() {
		return convalida;
	}

	/**
	 * Sets the convalida.
	 *
	 * @param convalida the convalida to set
	 */
	public void setConvalida(boolean convalida) {
		this.convalida = convalida;
	}

	/**
	 * @return the annoBilancio
	 */
	public Integer getAnnoBilancio() {
		return this.annoBilancio;
	}

	/**
	 * @param annoBilancio the annoBilancio to set
	 */
	public void setAnnoBilancio(Integer annoBilancio) {
		this.annoBilancio = annoBilancio;
	}
	
	/**
	 * @return the flagSoggettoDurc
	 */
	public String getFlagSoggettoDurc() {
		return flagSoggettoDurc;
	}

	/**
	 * @param flagSoggettoDurc the flagSoggettoDurc to set
	 */
	public void setFlagSoggettoDurc(String flagSoggettoDurc) {
		this.flagSoggettoDurc = flagSoggettoDurc;
	}

	/**
	 * @return the descrizioneCompletaSoggetto
	 */
	public String getDescrizioneCompletaSoggetto() {
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto()) || StringUtils.isBlank(soggetto.getDenominazione()) || StringUtils.isBlank(soggetto.getCodiceFiscale())) {
			return "";
		}
		return soggetto.getCodiceSoggetto() + " - " + soggetto.getDenominazione() + " - " + soggetto.getCodiceFiscale();
	}
	
	/**
	 * @return the descrizioneCompletaMovimentoGestione
	 */
	public String getDescrizioneCompletaMovimentoGestione() {
		if(impegno == null || impegno.getAnnoMovimento() == 0 || impegno.getNumero() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(": ")
				.append(impegno.getAnnoMovimento())
				.append(" / ")
				.append(impegno.getNumero().toPlainString());
		if(subImpegno != null && subImpegno.getNumero() != null) {
			sb.append(" - ")
				.append(subImpegno.getNumero().toPlainString());
		}
		return sb.toString();
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public RicercaAllegatoAtto creaRequestRicercaAllegatoAtto() {
		RicercaAllegatoAtto request = creaRequest(RicercaAllegatoAtto.class);
		
		// Popolamento dell'allegato
		populateAllegatoAtto();
		
		request.setAllegatoAtto(getAllegatoAtto());
		request.setDataScadenzaDa(getDataScadenzaDa());
		request.setDataScadenzaA(getDataScadenzaA());
		request.setElencoDocumentiAllegato(impostaEntitaFacoltativa(getElencoDocumentiAllegato()));
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setFlagRitenute(FormatUtils.parseBooleanSN(getFlagRitenute()));
		request.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		request.setImpegno(impostaEntitaFacoltativa(getImpegno()));
		request.setSubImpegno(impostaEntitaFacoltativa(getSubImpegno()));
		request.setListaAttoAmministrativo(getListaAttoAmministrativo());
		
		// SIAC-6166
		if(getAnnoBilancio() != null) {
			request.setBilancio(new Bilancio());
			request.getBilancio().setAnno(getAnnoBilancio().intValue());
		}
		
		request.setAllegatoAttoModelDetails(
				AllegatoAttoModelDetail.IsAssociatoAdAlmenoUnaQuotaSpesa,
				AllegatoAttoModelDetail.IsAssociatoAdUnDocumento,
				AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoSospeso,
				AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoConOrdinativo,
				AllegatoAttoModelDetail.HasImpegnoConfermaDurcDataFineValidita);
		
		return request;
	}

	/**
	 * Popola l'allegato atto
	 */
	private void populateAllegatoAtto() {
		getAllegatoAtto().setEnte(getEnte());
		/*
 		// Se il soggetto e' valorizzato, lo aggiungo ai parametri di ricerca. Altrimenti lo ignoro
		Soggetto soggettoDaImpostare = impostaEntitaFacoltativa(getSoggetto());
		
		if(soggettoDaImpostare != null) {
			DatiSoggettoAllegato datiSoggettiAllegati = new DatiSoggettoAllegato();
			datiSoggettiAllegati.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
			getAllegatoAtto().addDatiSoggettoAllegato(datiSoggettiAllegati);
		}
		*/
		getAllegatoAtto().setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		//SIAC-6261
		getAllegatoAtto().setHasImpegnoConfermaDurc(FormatUtils.parseBooleanSN(getFlagSoggettoDurc()));
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		
		request.setEnte(getEnte());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}
	
	@Override
	public RicercaElenco creaRequestRicercaElenco() {
		RicercaElenco request = super.creaRequestRicercaElenco();
		
		// Metto tutti gli stati
		request.setStatiOperativiElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.values()));
		
		return request;
	}

	/* **** Utilities **** */
	/**
	 * Compone la stringa di riepilogo per la ricerca.
	 * 
	 * @param listaTipoAtto                         la lista dei tipi di atto
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture amministrativo contabili
	 * 
	 * @return la stringa di riepilogo
	 */
	public String componiStringaRiepilogo(List<TipoAtto> listaTipoAtto, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		final StringBuilder sb = new StringBuilder();
		final String delim = " - ";
		
		//SIAC-6166
		if(getAnnoBilancio() != null) {
			sb.append("ANNO DI BILANCIO: ")
				.append(getAnnoBilancio())
				.append(delim);
		}
		
		if(StringUtils.isNotBlank(getAllegatoAtto().getCausale())) {
			sb.append("CAUSALE: ")
				.append(getAllegatoAtto().getCausale())
				.append(delim);
		}
		if(getAllegatoAtto().getStatoOperativoAllegatoAtto() != null) {
			sb.append("STATO: ")
				.append(getAllegatoAtto().getStatoOperativoAllegatoAtto().getDescrizione())
				.append(delim);
		}
		if(getDataScadenzaDa() != null) {
			sb.append("DATA SCADENZA DA: ")
				.append(FormatUtils.formatDate(getDataScadenzaDa()));
			if(getDataScadenzaA() != null) {
				sb.append(" A: ")
					.append(FormatUtils.formatDate(getDataScadenzaA()));
			}
			sb.append(delim);
		} else if(getDataScadenzaA() != null) {
			sb.append("DATA SCADENZA A: ")
				.append(FormatUtils.formatDate(getDataScadenzaA()))
				.append(delim);
		}

		final String provvedimentoDelim = "/";

		if(getAttoAmministrativo() != null && getAttoAmministrativo().getUid() != 0) {
			TipoAtto ta = ComparatorUtils.searchByUid(listaTipoAtto, getAttoAmministrativo().getTipoAtto());
			StrutturaAmministrativoContabile sac =ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, getAttoAmministrativo().getStrutturaAmmContabile());
			sb.append("PROVVEDIMENTO: ")
				.append(getAttoAmministrativo().getAnno())
				.append(provvedimentoDelim)
				.append(getAttoAmministrativo().getNumero())
				.append(provvedimentoDelim);
				if(getListaAttoAmministrativo() != null && getListaAttoAmministrativo().size()==1) {
					sb.append(ta.getCodice());
					if(sac != null) {
						sb.append(provvedimentoDelim)
							.append(sac.getCodice());
					}
				}
			sb.append(delim);	
		}
		
		if(getSoggetto() != null && getSoggetto().getUid() != 0) {
			sb.append("SOGGETTO: ")
				.append(getSoggetto().getCodiceSoggetto())
				.append(delim);
		}
		if(getImpegno() != null && getImpegno().getUid() != 0) {
			sb.append("IMPEGNO: ")
				.append(getImpegno().getAnnoMovimento())
				.append("/")
				.append(getImpegno().getNumero().toPlainString());
			sb.append(delim);
		}
		if(getElencoDocumentiAllegato() != null && getElencoDocumentiAllegato().getUid() != 0) {
			sb.append("ELENCO: ")
				.append(getElencoDocumentiAllegato().getAnno())
				.append("/")
				.append(getElencoDocumentiAllegato().getNumero())
				.append(delim);
		}
		
		return StringUtils.substringBeforeLast(sb.toString(), delim);
	}

	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), getImpegno(), getSubImpegno());
	}
	
}
