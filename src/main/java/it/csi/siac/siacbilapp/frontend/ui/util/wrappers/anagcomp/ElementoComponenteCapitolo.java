/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagcomp;

 

 

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * Wrapper per TipoComponenteImportiCapitolo.
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 27/set/2019
 *
 */
/**
 * @author filippo
 *
 */
public class ElementoComponenteCapitolo implements ModelWrapper, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4925124192153155721L;


 
	
	private final TipoComponenteImportiCapitolo componenteCapitolo;
	private String azioni;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoComponenteCapitolo(TipoComponenteImportiCapitolo componenteCapitolo) {
		this.componenteCapitolo = componenteCapitolo;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return componenteCapitolo == null ? 0 : componenteCapitolo.getUid();
	}
	
	// Utilita' per il Javascript
 
	/**
	 * @return the stringaCodice
	 */
	public String getStringaCodice() {
		return componenteCapitolo != null ? componenteCapitolo.getCodice() : "";
	}
	
	public String getStringaMacrotipo() {
		return componenteCapitolo != null && componenteCapitolo.getMacrotipoComponenteImportiCapitolo() != null ? componenteCapitolo.getMacrotipoComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	public String getStringaSottotipo() {
		return componenteCapitolo != null && componenteCapitolo.getSottotipoComponenteImportiCapitolo() != null ? componenteCapitolo.getSottotipoComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	public String getStringaDescrizione() {
		return componenteCapitolo != null ? componenteCapitolo.getDescrizione() : "";
	}
	
	public String getStringaStato() {
		return componenteCapitolo != null && componenteCapitolo.getStatoTipoComponenteImportiCapitolo() != null ? componenteCapitolo.getStatoTipoComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	
	public String getStringaAmbito() {
		return componenteCapitolo != null && componenteCapitolo.getAmbitoComponenteImportiCapitolo() !=null ? componenteCapitolo.getAmbitoComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	public String getStringaFonte() {
		return componenteCapitolo != null && componenteCapitolo.getFonteFinanziariaComponenteImportiCapitolo() != null ? componenteCapitolo.getFonteFinanziariaComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	public String getStringaMomento() {
		return componenteCapitolo != null && componenteCapitolo.getMomentoComponenteImportiCapitolo() !=null ? componenteCapitolo.getMomentoComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	public String getStringaAnno() {
		return componenteCapitolo != null && componenteCapitolo.getAnno() != null ? componenteCapitolo.getAnno().toString() : "";
	}
	 

	public String getStringaDefault() {
		return componenteCapitolo != null && componenteCapitolo.getPropostaDefaultComponenteImportiCapitolo() != null ? componenteCapitolo.getPropostaDefaultComponenteImportiCapitolo().getDescrizione() : "";
		
	}
	//SIAC-7349
	public String getStringaImpegnabile() {
		return componenteCapitolo != null && componenteCapitolo.getImpegnabileComponenteImportiCapitolo() != null ? componenteCapitolo.getImpegnabileComponenteImportiCapitolo().getDescrizione() : "";
		
	}
	
	public String getStringaDataInizioValidita() {
		return componenteCapitolo != null ? FormatUtils.formatDate(componenteCapitolo.getDataInizioValidita()) : "";
	}
	
	public String getStringaDataFineValidita() {
		return componenteCapitolo != null ? FormatUtils.formatDate(componenteCapitolo.getDataFineValidita()) : "";
	}
	/**
	 * Controlla se il vincolo &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del vincolo &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean isAnnullato() {
		return StatoTipoComponenteImportiCapitolo.ANNULLATO.equals( componenteCapitolo.getStatoTipoComponenteImportiCapitolo());
	}
	 
}
