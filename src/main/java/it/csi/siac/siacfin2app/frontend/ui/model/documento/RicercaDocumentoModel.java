/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;


/**
  *  Classe di model generica per la ricerca del documento.
  * 
  * @author Osorio Alessandra
  * @version 1.0.0 - 10/03/2014
  *
  */
public abstract class RicercaDocumentoModel extends GenericDocumentoModel {
			
	/** Per la serializzazione */
	private static final long serialVersionUID = 7327741641707476224L;
	
	/* Label per la specializzazione della pagina risultante: 
	 * 			 la ricerca Entrata / Spesa differisce unicamente per questi tre campi
	 */
	private String labelEntrataSpesa;
	private String labelRicercaEntrataSpesa;
	private String labelRilevanteIvaEntrataSpesa;
	
	// Flag che utilizzo successivamente per comporre la stringa risultante da mettere in sessione
	private Boolean campoFormPrincipalePresente;
	private Boolean provvedimentoPresente;
	private Boolean soggettoPresente;
	private Boolean movimentoPresente;
	private Boolean elencoPresente;
	
	// Per la ricerca di dettaglio
	private Integer uidDocumento;
	
	private String flagIva;
	
	/** Costruttore vuoto di default */
	public RicercaDocumentoModel() {
		setTitolo("Ricerca Documenti");
	}

	/**
	 * @return the labelEntrataSpesa
	 */
	public String getLabelEntrataSpesa() {
		return labelEntrataSpesa;
	}

	/**
	 * @param labelEntrataSpesa the labelEntrataSpesa to set
	 */
	public void setLabelEntrataSpesa(String labelEntrataSpesa) {
		this.labelEntrataSpesa = labelEntrataSpesa;
	}

	/**
	 * @return the labelRicercaEntrataSpesa
	 */
	public String getLabelRicercaEntrataSpesa() {
		return labelRicercaEntrataSpesa;
	}

	/**
	 * @param labelRicercaEntrataSpesa the labelRicercaEntrataSpesa to set
	 */
	public void setLabelRicercaEntrataSpesa(String labelRicercaEntrataSpesa) {
		this.labelRicercaEntrataSpesa = labelRicercaEntrataSpesa;
	}

	/**
	 * @return the labelRilevanteIvaEntrataSpesa
	 */
	public String getLabelRilevanteIvaEntrataSpesa() {
		return labelRilevanteIvaEntrataSpesa;
	}

	/**
	 * @param labelRilevanteIvaEntrataSpesa the labelRilevanteIvaEntrataSpesa to set
	 */
	public void setLabelRilevanteIvaEntrataSpesa(String labelRilevanteIvaEntrataSpesa) {
		this.labelRilevanteIvaEntrataSpesa = labelRilevanteIvaEntrataSpesa;
	}

	/**
	 * @return the campoFormPrincipalePresente
	 */
	public Boolean getCampoFormPrincipalePresente() {
		return campoFormPrincipalePresente;
	}

	/**
	 * @param campoFormPrincipalePresente the campoFormPrincipalePresente to set
	 */
	public void setCampoFormPrincipalePresente(Boolean campoFormPrincipalePresente) {
		this.campoFormPrincipalePresente = campoFormPrincipalePresente;
	}

	/**
	 * @return the provvedimentoPresente
	 */
	public Boolean getProvvedimentoPresente() {
		return provvedimentoPresente;
	}

	/**
	 * @param provvedimentoPresente the provvedimentoPresente to set
	 */
	public void setProvvedimentoPresente(Boolean provvedimentoPresente) {
		this.provvedimentoPresente = provvedimentoPresente;
	}

	/**
	 * @return the soggettoPresente
	 */
	public Boolean getSoggettoPresente() {
		return soggettoPresente;
	}

	/**
	 * @param soggettoPresente the soggettoPresente to set
	 */
	public void setSoggettoPresente(Boolean soggettoPresente) {
		this.soggettoPresente = soggettoPresente;
	}

	/**
	 * @return the movimentoPresente
	 */
	public Boolean getMovimentoPresente() {
		return movimentoPresente;
	}

	/**
	 * @param movimentoPresente the movimentoPresente to set
	 */
	public void setMovimentoPresente(Boolean movimentoPresente) {
		this.movimentoPresente = movimentoPresente;
	}

	/**
	 * @return the elencoPresente
	 */
	public Boolean getElencoPresente() {
		return elencoPresente;
	}

	/**
	 * @param elencoPresente the elencoPresente to set
	 */
	public void setElencoPresente(Boolean elencoPresente) {
		this.elencoPresente = elencoPresente;
	}

	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		return uidDocumento;
	}

	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(Integer uidDocumento) {
		this.uidDocumento = uidDocumento;
	}

	/**
	 * @return the flagIva
	 */
	public String getFlagIva() {
		return flagIva;
	}

	/**
	 * @param flagIva the flagIva to set
	 */
	public void setFlagIva(String flagIva) {
		this.flagIva = flagIva;
	}

	/**
	 * Compone una stringa con i parametri di ricerca impostati.
	 * 
	 * @param documento                             il documento
	 * @param listaTipoDoc                          la lista dei tipi di documento
	 * @param listaTipoAtto                         la lista dei tipi di atto
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture amministativo contabili
	 * 
	 * @return la stringa di riepilogo 
	 */
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto, 
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		if(Boolean.TRUE.equals(getCampoFormPrincipalePresente())) {
			componiStringaRiepilogoCampiFormPrincipale(documento, listaTipoDoc, components);
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			componiStringaRiepilogoElenco(components);
		}
		if(Boolean.TRUE.equals(getMovimentoPresente())) {
			componiStringaRiepilogoMovimento(components);
		}
		if(areDatiProtocolloPresenti(documento)) {
			componiStringaRiepilogoDatiProtocollo(documento, components);
		}
		
		if(Boolean.TRUE.equals(getProvvedimentoPresente())) {
			componiStringaRiepilogoProvvedimento(listaTipoAtto, listaStrutturaAmministrativoContabile, components);
		}
		
		if(Boolean.TRUE.equals(getSoggettoPresente())) {
			componiStringaRiepilogoSoggetto(documento, components);
		}
		
		return StringUtils.join(components, " - ");
	}

	/**
	 * Compone la stringa di riepilogo per i campi del form principale e li injetta nell'array dei componenti
	 * 
	 * @param documento    il documento da cui ottenere i dati
	 * @param listaTipoDoc la lista dei tipi documento da cui ottenere i dati per il tipo
	 * @param components   la lista delle componenti della stringa di riepilogo
	 */
	private <D extends Documento<?, ?>> void componiStringaRiepilogoCampiFormPrincipale(D documento, List<TipoDocumento> listaTipoDoc, List<String> components) {
		if(documento.getTipoDocumento() != null && documento.getTipoDocumento().getUid() != 0) {
			TipoDocumento tipo = ComparatorUtils.searchByUid(listaTipoDoc, documento.getTipoDocumento());
			components.add("Tipo: " + tipo.getCodice() + " - " + tipo.getDescrizione());
		}
		if(documento.getStatoOperativoDocumento() != null) {
			components.add("Stato: " + documento.getStatoOperativoDocumento().getCodice() + " - " + documento.getStatoOperativoDocumento().getDescrizione());
		}
		if(documento.getAnno() != null) {
			components.add("Anno: " + documento.getAnno());
		}
		if(StringUtils.isNotBlank(documento.getNumero())) {
			components.add("Numero: " + documento.getNumero());
		}
		// Data emissione
		if(documento.getDataEmissione() != null) {
			components.add("Data: " + FormatUtils.formatDate(documento.getDataEmissione()));
		}
	}

	/**
	 * Compone la stringa di riepilogo per i campi dell'elenco e li injetta nell'array dei componenti
	 * 
	 * @param components la lista delle componenti della stringa di riepilogo
	 */
	private void componiStringaRiepilogoElenco(List<String> components) {
		List<String> subComponents = new ArrayList<String>();
		if(getElencoDocumenti().getAnno() != null) {
			subComponents.add(getElencoDocumenti().getAnno().toString());
		}
		if(getElencoDocumenti().getNumero() != null) {
			subComponents.add(getElencoDocumenti().getNumero().toString());
		}
		
		components.add("Elenco: " + StringUtils.join(subComponents, "/"));
	}

	/**
	 * Compone la stringa di riepilogo per i campi del movimento e li injetta nell'array dei componenti
	 * 
	 * @param components la lista delle componenti della stringa di riepilogo
	 */
	protected abstract void componiStringaRiepilogoMovimento(List<String> components);

	/**
	 * Controlla se i dati del protocollo siano presenti.
	 * 
	 * @param documento il documento da controllare
	 * @return <code>true</code> se i dati sono presenti e valorizzati; <code>false</code> altrimenti
	 */
	public boolean areDatiProtocolloPresenti(Documento<?, ?> documento) {
		return documento != null && (documento.getAnnoRepertorio() != null || documento.getDataRepertorio() != null
				|| StringUtils.isNotBlank(documento.getNumeroRepertorio()) || StringUtils.isNotBlank(documento.getRegistroRepertorio()));
	}

	/**
	 * Compone la stringa di riepilogo per i campi protocollo e li injetta nell'array dei componenti
	 * 
	 * @param documento  il documento da cui ottenere i dati
	 * @param components la lista delle componenti della stringa di riepilogo
	 */
	private <D extends Documento<?, ?>> void componiStringaRiepilogoDatiProtocollo(D documento, List<String> components) {
		List<String> subcomponents = new ArrayList<String>();
		
		if(documento.getAnnoRepertorio() != null) {
			subcomponents.add("Anno: " + documento.getAnnoRepertorio());
		}
		if(documento.getDataRepertorio() != null) {
			subcomponents.add("Data: " + FormatUtils.formatDate(documento.getDataRepertorio()));
		}
		if(StringUtils.isNotBlank(documento.getNumeroRepertorio())) {
			subcomponents.add("Numero: " + documento.getNumeroRepertorio());
		}
		if(StringUtils.isNotBlank(documento.getRegistroRepertorio())) {
			subcomponents.add("Registro (AOO): " + documento.getRegistroRepertorio());
		}
		
		components.add("Protocollo: " + StringUtils.join(subcomponents, " - "));
	}

	/**
	 * Compone la stringa di riepilogo per i campi del provvedimento e li injetta nell'array dei componenti
	 * 
	 * @param listaTipoAtto                         la lista dei tipi di atto da cui recuperare le informazioni
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture amministrativo contabili da cui recuperare le informazioni
	 * @param components                            la lista delle componenti della stringa di riepilogo
	 */
	private void componiStringaRiepilogoProvvedimento(List<TipoAtto> listaTipoAtto, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, List<String> components) {
		StringBuilder sb = new StringBuilder()
			.append("Provvedimento: ")
			.append(getAttoAmministrativo().getAnno())
			.append("/")
			.append(getAttoAmministrativo().getNumero());
		
		TipoAtto tipoAtto = ComparatorUtils.searchByUid(listaTipoAtto, getAttoAmministrativo().getTipoAtto());
		if(tipoAtto != null && StringUtils.isNotBlank(tipoAtto.getDescrizione())){
			sb.append(" - ").append(tipoAtto.getDescrizione());
		}
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile =
			ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, getAttoAmministrativo().getStrutturaAmmContabile());
		
		if(strutturaAmministrativoContabile != null) {
			sb.append(" - ").append("Struttura Amministrativa: " + strutturaAmministrativoContabile.getCodice());
		}
		components.add(sb.toString());
	}

	/**
	 * Compone la stringa di riepilogo per i campi del soggetto e li injetta nell'array dei componenti
	 * 
	 * @param documento  il documento da cui ottenere i dati
	 * @param components la lista delle componenti della stringa di riepilogo
	 */
	private <D extends Documento<?, ?>> void componiStringaRiepilogoSoggetto(D documento, List<String> components) {
		components.add("Codice Soggetto: " + documento.getSoggetto().getCodiceSoggetto());
	}

}
