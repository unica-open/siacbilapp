/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.registroa;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespite;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCredito;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di model per la ricerca del registro A del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
public class RisultatiRicercaRegistroACespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1813709379633424984L;
	private PrimaNota primaNota;
	private Integer savedDisplayStart;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistroACespiteModel() {
		setTitolo("Risultati ricerca registro prime note definitive verso inventario contabile");
	}
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return this.primaNota;
	}
	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}
	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return this.savedDisplayStart;
	}
	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}
	/**
	 * Creazione della request per il rifiuto della prima nota cespite a partire dalla contabilit&agrave; generale
	 * @return la request create
	 */
	public RifiutaPrimaNotaSuRegistroACespite creaRequestRifiutaPrimaNotaSuRegistroACespite() {
		RifiutaPrimaNotaSuRegistroACespite req = creaRequest(RifiutaPrimaNotaSuRegistroACespite.class);
		req.setPrimaNota(getPrimaNota());
		return req;
	}
	
	/**
	 * Creazione della request per la validazione della prima nota cespite a partire dalla contabilit&agrave; generale
	 * @return la request create
	 */
	public InserisciPrimaNotaSuRegistroACespite creaRequestInserisciPrimaNotaSuRegistroACespite() {
		InserisciPrimaNotaSuRegistroACespite req = creaRequest(InserisciPrimaNotaSuRegistroACespite.class);
		req.setPrimaNota(getPrimaNota());
		return req;
	}
	
	
	/**
	 * Crea request ottieni dati prime note fattura con nota credito.
	 *
	 * @return the ottieni dati prime note fattura con nota credito
	 */
	public OttieniDatiPrimeNoteFatturaConNotaCredito creaRequestOttieniDatiPrimeNoteFatturaConNotaCredito() {
		OttieniDatiPrimeNoteFatturaConNotaCredito req = creaRequest(OttieniDatiPrimeNoteFatturaConNotaCredito.class);
		req.setPrimaNota(getPrimaNota());
		return req;
	}
	
}
